import java.util.HashMap;
import java.util.Map;

/**
 * book_my_stay - Hotel Booking Management System
 * Use Case 3: Centralized Room Inventory Management
 * @author gayatrik0027
 * @version 3.0
 */

// Abstract Class from UC2
abstract class Room {
    protected String roomType;
    protected double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single", 1000.0); }
    public void displayDetails() { System.out.println(roomType + " Room | Price: Rs." + price); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double", 1800.0); }
    public void displayDetails() { System.out.println(roomType + " Room | Price: Rs." + price); }
}

// --- NEW INVENTORY COMPONENT ---
class RoomInventory {
    // HashMap to store roomType -> availableCount
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Method to register/add rooms to inventory
    public void addRooms(String type, int count) {
        inventory.put(type, count);
    }

    // Method to check availability
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Method to display the whole inventory
    public void displayInventory() {
        System.out.println("Current Inventory Status:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("- " + entry.getKey() + " Rooms: " + entry.getValue() + " available");
        }
    }
}

public class book_my_stay {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      Welcome to Book My Stay App         ");
        System.out.println("            Version: 3.0                  ");
        System.out.println("==========================================");

        // Initialize Inventory
        RoomInventory hotelInventory = new RoomInventory();

        // Registering room types with their counts
        hotelInventory.addRooms("Single", 10);
        hotelInventory.addRooms("Double", 5);
        hotelInventory.addRooms("Suite", 2);

        // Displaying current state
        hotelInventory.displayInventory();

        System.out.println("==========================================");
        System.out.println("Check: Availability for Double Rooms is " + hotelInventory.getAvailability("Double"));
        System.out.println("==========================================");
    }
}