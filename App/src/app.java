import java.util.*;

// 1. Represents a Booking record capable of being cancelled
class Booking {
    private String id;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Booking(String id, String roomType, String roomId) {
        this.id = id;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getId() { return id; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return isCancelled; }

    public void markAsCancelled() { this.isCancelled = true; }
}

// 2. Cancellation Service: Manages the controlled rollback of system state
class CancellationService {
    // Stack used for LIFO Rollback: recently released rooms are tracked here
    private Stack<String> releasedRoomIds = new Stack<>();
    private Map<String, Integer> inventory; // Reference to core inventory

    public CancellationService(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    // Controlled Mutation: Reversing state in a strict order
    public void cancelBooking(Booking booking) throws Exception {
        // 1. Validation: Ensure reservation exists and isn't already cancelled
        if (booking == null || booking.isCancelled()) {
            throw new Exception("Error: Invalid or already cancelled booking.");
        }

        System.out.println("Processing Cancellation for: " + booking.getId());

        // 2. State Reversal: Record the allocated room ID in rollback structure
        releasedRoomIds.push(booking.getRoomId());

        // 3. Inventory Restoration: Increment count for the room type
        String type = booking.getRoomType();
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);

        // 4. Update Status: Finalize the state change
        booking.markAsCancelled();

        System.out.println("Success: Room " + booking.getRoomId() + " returned to pool.");
    }

    public Stack<String> getReleasedRoomIds() {
        return releasedRoomIds;
    }
}

// 3. Main Application Class
public class App {
    public static void main(String[] args) {
        // Mock Inventory State
        Map<String, Integer> currentInventory = new HashMap<>();
        currentInventory.put("DELUXE", 5);

        CancellationService cancellationService = new CancellationService(currentInventory);

        // Scenario: A guest has a confirmed booking
        Booking activeBooking = new Booking("BK-999", "DELUXE", "ROOM-101");

        System.out.println("Initial Inventory: " + currentInventory.get("DELUXE"));

        try {
            // Perform Cancellation
            cancellationService.cancelBooking(activeBooking);

            // Verify Results
            System.out.println("Updated Inventory: " + currentInventory.get("DELUXE"));
            System.out.println("Last Released Room ID: " + cancellationService.getReleasedRoomIds().peek());

            // Attempt Duplicate Cancellation (Validation Test)
            cancellationService.cancelBooking(activeBooking);

        } catch (Exception e) {
            System.err.println("CANCELLATION FAILED: " + e.getMessage());
        }
    }
}