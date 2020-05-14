public class Clock extends Thread {
    public static long time = System.currentTimeMillis();
    public static boolean boardingTime = true;
    public static boolean disembarkTime = true;


    public Clock() {
        boardingTime = false;
        disembarkTime = false;
        start();
    }

    public void run() {

        while(Clerk.readyForBoarding) {
            //sleep for a fixed amount of time before and in between boarding and disembarking
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msg("Half an hour before plane departs");
            boardingTime = true;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            disembarkTime = true;
            msg("is terminating");
        }
    }
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "Clock: " + m);
    }
}