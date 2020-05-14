import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;

public class Clerk extends Thread {

    public static long time = System.currentTimeMillis();
    private int num;
    private boolean readyToLeave = false;
    public Random rand = new Random();
    public static LinkedList<Passenger> readyToBoard = new LinkedList<>();
    public static boolean readyForBoarding = false;
    public static int seatNumber;
    public static int zoneNumber;

    public Clerk(int num) {
        this.num = num;
        setName("Clerk-" + num);
        start();
    }

    public void run() {
        msg("waiting for passengers");
        while(true) {
            if(Passenger.onLine) {
                msg("This is the size of the kiosk" + Project1.kiosk.size());
            }
            for(int i = 0; i < Project1.kiosk.size(); i++) {
                Passenger p = kioskClerk();
                msg("now serving Passenger-" + p.getNum());
                giveBoardingPass();
                readyToBoard.add(p);
                readyForBoarding = true;
            }
            if(servedAllPassengers()) {
                readyToLeave = true;
                break;
            }
        }
    }

    public void giveBoardingPass() {
        for(int i = 0; i < Project1.numPassengers; i++) {
            seatNumber = rand.nextInt(30);
            if(seatNumber > 0 && seatNumber < 11) {
                zoneNumber = 1;
            } else if (seatNumber > 10 && seatNumber < 21) {
                zoneNumber = 2;
            } else {
                zoneNumber = 3;
            }
        }
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "Clerk-" + num + ": " + m);
    }

    public Passenger kioskClerk() {
        return Project1.kiosk.remove(0);
    }

    public boolean servedAllPassengers() {
        if(Passenger.counter == Project1.numPassengers) return true;
        return false;
    }
}
