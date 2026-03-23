import java.io.*;
import java.util.*;

// --- 1. DOMAIN MODELS ---
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String roomType;
    public boolean isCancelled = false;

    public Booking(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
    }
}

// --- 2. CUSTOM EXCEPTIONS ---
class BookingException extends Exception {
    public BookingException(String message) { super(message); }
}

// --- 3. THE CORE ENGINE (APP) ---
public class App {
    private static final String SAVE_FILE = "system_state.ser";
    private Map<String, Integer> inventory = new HashMap<>();
    private List<Booking> history = new ArrayList<>();

    public static void main(String[] args) {
        App hotelSystem = new App();
        hotelSystem.run();
    }

    public void run() {
        // LOAD: Recovery Logic
        loadFromDisk();

        // EXECUTE: Simulation of Business Logic
        try {
            System.out.println("--- Current Inventory: " + inventory + " ---");

            // Validate & Process
            processNewBooking("BK-101", "DELUXE");
            processNewBooking("BK-102", "DELUXE");

            // Test Cancellation
            cancelBooking("BK-101");

        } catch (BookingException e) {
            System.err.println("VALIDATION ERROR: " + e.getMessage());
        }

        // SAVE: Persistence Logic
        saveToDisk();
        System.out.println("--- System Shutdown Cleanly ---");
    }

    // BUSINESS LOGIC with Validation (UC9)
    private void processNewBooking(String id, String type) throws BookingException {
        if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
            throw new BookingException("No inventory available for " + type);
        }
        inventory.put(type, inventory.get(type) - 1);
        history.add(new Booking(id, type));
        System.out.println("Confirmed: " + id);
    }

    // STATE REVERSAL (UC10)
    private void cancelBooking(String id) throws BookingException {
        Booking b = history.stream()
                .filter(book -> book.id.equals(id) && !book.isCancelled)
                .findFirst()
                .orElseThrow(() -> new BookingException("Booking not found or already cancelled"));

        b.isCancelled = true;
        inventory.put(b.roomType, inventory.get(b.roomType) + 1);
        System.out.println("Cancelled & Reverted: " + id);
    }

    // PERSISTENCE (UC12)
    private void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("Data Saved.");
        } catch (IOException e) {
            System.err.println("Save Failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromDisk() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            inventory.put("DELUXE", 5); // Default start state
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            inventory = (Map<String, Integer>) ois.readObject();
            history = (List<Booking>) ois.readObject();
            System.out.println("Data Restored.");
        } catch (Exception e) {
            System.err.println("Recovery Failed. Starting fresh.");
            inventory.put("DELUXE", 5);
        }
    }
}