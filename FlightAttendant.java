public class FlightAttendant extends Thread {

    public static long time = System.currentTimeMillis();
    public static boolean boarding = false;
    public static boolean onLine = false;
    public static int countLine = 0;
    public static boolean readyToBoard = false;
    public static boolean doorOpen = true;
    public static boolean onPlane = false;
    public static boolean planeLands = false;
    public static boolean seatBelt = true;
    public static boolean readyToLeave = false;

    public FlightAttendant() {
        setName("Flight Attendant");
        start();
    }

    public void run () {

        while(true) {
            while(!readyToLeave && Clerk.readyForBoarding) {
            /*
            for each of the passengers, if their zone matches, add them to the line
            if the line reaches 10 (the complete zone),
            */
                if(Clock.boardingTime) boarding = true;
                for(int i = 0; i < Project1.numPassengers; i++) {
                    for(int k = 1; k <= 3; k++) {
                        if (Clerk.readyToBoard.get(i).zoneNumber == k) {
                            Project1.onPlane.add(Clerk.readyToBoard.get(i));
                            onLine = true;
                        }
                    }
                    readyToBoard = true;
                }
                readyToBoard = false;
                msg("The door of the plane has closed");
                doorOpen = false;
                onPlane = true;

                //flight attendant wakes up passengers with an announcement over the loud speaker
                for(int i = 0; i < Project1.passengers.length; i++) {
                    Project1.passengers[i].interrupt();
                }

                planeLands = true;
                if(Clock.disembarkTime) seatBelt = false;

                //once the seatbelt light is off, the passengers can disembark (exits busy wait)
                if(!seatBelt) planeLands = false;

                //ascending order of their seat number
                for(int i = 0; i < Project1.passengers.length; i++) {
                    if(Project1.passengers[i].isAlive()) {
                        try {
                            Project1.passengers[i].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //finding passenger with lowest seat number
                Passenger minSeatNumber = Project1.onPlane.get(0);
                Passenger temp = null;
                while(!Project1.onPlane.isEmpty()) {
                    for(int i = 0; i < Project1.onPlane.size(); i++) {
                        if(Project1.onPlane.get(i).getSeatNumber() < minSeatNumber.getSeatNumber() && Project1.onPlane.get(i).isAlive()) {
                            minSeatNumber = Project1.onPlane.get(i);
                        }
                        temp = Project1.onPlane.remove(i);
                    }
                    try {
                        temp.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Clerk.readyForBoarding = false;

                //flightAttendant cleans aircraft and leaves after all passengers terminate
                for(int i = 0; i < Project1.numPassengers; i++) {
                    if(Clerk.readyToBoard.isEmpty()) readyToLeave = true;
                }
            }
        }


    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "Flight Attendant: " + m);
    }
}
