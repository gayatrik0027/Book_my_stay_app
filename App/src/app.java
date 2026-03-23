import java.util.*;

public class app {

    // Simulated inventory (roomType -> available count)
    static Map<String, Integer> inventory = new HashMap<>();

    // roomType -> set of allocated room IDs
    static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Queue for booking requests (FIFO)
    static Queue<String> bookingQueue = new LinkedList<>();

    public static void main(String[] args) {

        // Initialize inventory
        inventory.put("DELUXE", 2);
        inventory.put("STANDARD", 1);

        // Add booking requests to queue
        bookingQueue.add("DELUXE");
        bookingQueue.add("DELUXE");
        bookingQueue.add("STANDARD");
        bookingQueue.add("DELUXE"); // should fail (no rooms left)

        processBookings();
    }

    // Process booking requests
    public static void processBookings() {
        while (!bookingQueue.isEmpty()) {
            String roomType = bookingQueue.poll();

            synchronized (App.class) { // atomic block

                int available = inventory.getOrDefault(roomType, 0);

                if (available > 0) {
                    String roomId = generateRoomId(roomType);

                    // Ensure uniqueness using Set
                    allocatedRooms
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Decrement inventory immediately
                    inventory.put(roomType, available - 1);

                    System.out.println("Reservation Confirmed: " + roomType + " -> " + roomId);
                } else {
                    System.out.println("Reservation Failed (No Availability): " + roomType);
                }
            }
        }
    }

    // Generate unique room ID
    public static String generateRoomId(String roomType) {
        return roomType + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}