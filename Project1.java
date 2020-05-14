import java.util.LinkedList;
import java.util.Queue;

public class Project1 {
    public static long time = System.currentTimeMillis();
    public static Thread[] clerks;
    public static Thread flightAttendant;
    public static Thread[] passengers;
    public static Thread clock;
    public static LinkedList<Passenger> kiosk = new LinkedList<>();
    public static LinkedList<Passenger> onPlane = new LinkedList<>();

    //global variables
    public static int counterNum;
    public static int numPassengers;
    public static int groupNum;
    private static Object Thread;

    public static void main (String[] args) {

        //initial values
        numPassengers = 30;
        groupNum = 4;
        counterNum = 3;

        //if command line overrides exist, replace numPassenger
        if(args.length > 0) {
            numPassengers = Integer.parseInt(args[0]);
        }

        //new clock thread
        clock = new Thread(new Clock());

        //create passenger threads
        passengers = new Thread[numPassengers];
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new Passenger(i++);
        }

        //create Clerk threads
        clerks = new Thread[2];
        for(int i = 0; i < 2; i++) {
            clerks[i] = new Thread(new Clerk(i++));
        }

        //new Flight Attendant thread
        flightAttendant = new Thread(new FlightAttendant());


    }
}