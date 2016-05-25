//package newserver;
package server;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class TimelyCheck extends Thread {

    HashMap<Long, String> ID_Table;
    //boolean run = true;

    public TimelyCheck(HashMap<Long, String> pID_Table) {
        ID_Table = pID_Table;
    }

    @Override
    public void run() {
        //while (run) {
            //System.out.println("im printing");
            for (Long Key : ID_Table.keySet()) {
                if (ID_Table.get(Key) == "terminate") {
                    for (Thread T : Thread.getAllStackTraces().keySet()) {
                        if (T.getId() == Key) {
                           if (!T.currentThread().isInterrupted()) {
                                T.stop();
                                ID_Table.remove(Key);
                                System.out.println("successfully");
                                System.out.println(T.getState());
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            
        //}
    }
}
