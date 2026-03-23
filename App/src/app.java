import java.util.*;

// Reservation class
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return guestName + " requested " + roomType;
    }
}

public class app {

    // FIFO Queue
    static Queue<Reservation> bookingQueue = new LinkedList<>();

    public static void main(String[] args) {

        addRequest("Prabhu", "DELUXE");
        addRequest("Arun", "STANDARD");
        addRequest("Divya", "DELUXE");

        showQueue();
    }

    // Add booking request
    public static void addRequest(String guestName, String roomType) {
        Reservation r = new Reservation(guestName, roomType);
        bookingQueue.add(r);
        System.out.println("Request Added: " + r);
    }

    // Display queue (FIFO order)
    public static void showQueue() {
        System.out.println("\nBooking Queue:");
        for (Reservation r : bookingQueue) {
            System.out.println(r);
        }
    }
}