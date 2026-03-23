import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    @Override
    public String toString() {
        return "Room Type: " + type +
                ", Price: " + price +
                ", Amenities: " + amenities;
    }
}

// Inventory: State Holder (read-only access during search)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability); // Defensive: read-only
    }
}

// Service: Search (read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> results = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {
            String roomType = entry.getKey();
            int availableCount = entry.getValue();

            // Validation Logic: only include available rooms
            if (availableCount > 0) {
                Room room = roomCatalog.get(roomType);

                // Defensive Programming: ensure room exists
                if (room != null) {
                    results.add(room);
                }
            }
        }

        return results;
    }
}

// App: Entry point (Guest interaction simulation)
public class app {
    public static void main(String[] args) {

        // Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // unavailable
        inventory.addRoom("Suite", 2);

        // Setup Room Catalog (Domain Objects)
        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", new Room("Single", 1000,
                Arrays.asList("WiFi", "TV")));
        roomCatalog.put("Double", new Room("Double", 1800,
                Arrays.asList("WiFi", "TV", "AC")));
        roomCatalog.put("Suite", new Room("Suite", 3000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Guest initiates search
        SearchService searchService = new SearchService(inventory, roomCatalog);
        List<Room> availableRooms = searchService.searchAvailableRooms();

        // Display results
        System.out.println("Available Rooms:");
        for (Room room : availableRooms) {
            System.out.println(room);
        }

        // Verify inventory unchanged (read-only behavior)
        System.out.println("\nInventory remains unchanged:");
        System.out.println(inventory.getAllAvailability());
    }
}