/**
 * book_my_stay - Hotel Booking Management System
 * Use Case 2: Basic Room Types & Static Availability
 * * @author gayatrik0027
 * @version 2.0
 */

// Abstract Class representing the general concept of a Room
abstract class book_my_stay {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public abstract void displayDetails();
}

// Concrete Class for Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
    @Override
    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: Rs." + price);
    }
}

// Concrete Class for Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
    @Override
    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: Rs." + price);
    }
}

// Concrete Class for Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3500.0);
    }
    @Override
    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: Rs." + price);
    }
}

public class book_my_stay {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      Welcome to Book My Stay App         ");
        System.out.println("            Version: 2.0                  ");
        System.out.println("==========================================");

        // Initializing Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability Variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display Room Details and Availability
        System.out.println("Available Room Types:");
        single.displayDetails();
        System.out.println("  Availability: " + singleAvailable);

        dbl.displayDetails();
        System.out.println("  Availability: " + doubleAvailable);

        suite.displayDetails();
        System.out.println("  Availability: " + suiteAvailable);

        System.out.println("==========================================");
    }
}