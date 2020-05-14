import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Passenger extends Thread {
    //Passenger variable
    public int num;
    public static boolean arrived = false;
    public static int counter = 0;
    private boolean boardingPass = false;
    public static int seatNumber;
    public static int zoneNumber;
    public static boolean sleeping = false;
    public static boolean counterFull = false;
    public static boolean onLine = false;
    public static int totalPassengersServed = Project1.numPassengers;
    public static long time = System.currentTimeMillis();

    public Passenger(int num) {
        this.num = num;
        setName("Passenger-" + num);
        arrived = true;
        start();
    }

    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 50000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("of Flight CS-340 to Purell-Wonderland, NY arrives at the airport and approaches the check-in counter");

        while (true) {
            //while the passengers are at the airport and the counter can accept them
            while (arrived && FlightAttendant.doorOpen && (totalPassengersServed > 0)) {

                //increment counter
                counter++;

                totalPassengersServed--;

                //if the counter has the allotted amount of customers, they will have to busy wait
                if (kioskAvailable()) {
                    msg("is on the check-in line");
                    Project1.kiosk.add(this);
                    msg("is now number " + Project1.kiosk.size() + " on the check-in line.");
                    onLine = true;
                } else {
                    counterFull = true;
                }
                //if the counter is full, busy wait
                while (counterFull) {
                }

                //receives boarding pass
                boardingPass = true;
                if(Clerk.readyForBoarding) {
                    seatNumber = Clerk.seatNumber;
                    zoneNumber = Clerk.zoneNumber;
                }

                //output message to screen with passenger's seat and zone information
                msg("Zone Number: " + zoneNumber + " || Seat Number: " + seatNumber);

                //passenger rushes to security
                rushToSecurity();

                msg("Waiting to board");

                //passengers arrive at gate and wait for flight attendant to call boarding
                while (!FlightAttendant.boarding) {}

                //flight attendant calls first zone and asks passengers to walk to the door and wait in line
                if (FlightAttendant.onLine) {
                    try {
                        Thread.sleep((long) Math.random() * 50000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //passengers busy wait until the other passengers arrive on the line
                while (waitOnLine()) {
                }

                //passengers enter the plane in groups
                for (int i = 0; i < Project1.groupNum; i++) {
                    //passengers scan boarding pass by using yield() twice
                    Thread.currentThread().yield();
                    Thread.currentThread().yield();
                }

                //while on the plane, passengers sleep on the flight on route to their destination
                while (FlightAttendant.onPlane) {
                    try {
                        Thread.sleep((long) Math.random() * 100000);
                        sleeping = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        msg("has been woken up");
                    }
                }

                //busy wait for the go-ahead to disembark the plane
                while (FlightAttendant.planeLands) {}

                //passengers disperse
                arrived = false;


            } //while
            if (!FlightAttendant.doorOpen) {
                msg("Plane doors have closed. Must rebook flight");
            } //if
        }


    } //run


    //simulate rushing by getting and setting priority
    public void rushToSecurity() {
        int priority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(priority + 1);

        //sleep for a random time
        try {
            Thread.sleep((long) Math.random() * 50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //setPriority back to default
        Thread.currentThread().setPriority(priority);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "Passenger-" + num + ": " + m);
    }

    public boolean waitOnLine() {
        if (FlightAttendant.readyToBoard) return true;
        return false;
    }

    public static boolean kioskAvailable() {
        if (Project1.kiosk.size() < Project1.counterNum) return true;
        return false;
    }

    public int getNum() {
        return this.num;
    }

    public int getSeatNumber() {
        return this.seatNumber;
    }

    public int getZoneNumber() {
        return this.zoneNumber;
    }
}