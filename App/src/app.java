import java.util.*;
import java.util.stream.Collectors;

// 1. Represents a confirmed reservation record
class Reservation {
    private String id;
    private String guestName;
    private String roomType;
    private double totalCost;

    public Reservation(String id, String guestName, String roomType, double totalCost) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
        this.totalCost = totalCost;
    }

    public String getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return String.format("[%s] Guest: %s | Room: %s | Paid: $%.2f",
                id, guestName, roomType, totalCost);
    }
}

// 2. Booking History: Maintains the chronological record of transactions
class BookingHistory {
    // List preserves insertion order, reflecting the real-world timeline
    private final List<Reservation> records = new ArrayList<>();

    public void archiveBooking(Reservation reservation) {
        records.add(reservation);
    }

    // Returns an unmodifiable list to ensure "Reporting" doesn't modify "History"
    public List<Reservation> getAllRecords() {
        return Collections.unmodifiableList(records);
    }
}

// 3. Booking Report Service: Generates summaries from history
class BookingReportService {
    private final BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void generateSummaryReport() {
        List<Reservation> data = history.getAllRecords();

        double totalRevenue = data.stream().mapToDouble(Reservation::getTotalCost).sum();
        long totalBookings = data.size();

        System.out.println("\n========== OPERATIONAL REPORT ==========");
        System.out.println("Total Bookings Processed: " + totalBookings);
        System.out.println("Total Revenue Generated:  $" + totalRevenue);
        System.out.println("----------------------------------------");

        // Grouping by Room Type (Reporting Readiness)
        Map<String, Long> roomPopularity = data.stream()
                .collect(Collectors.groupingBy(Reservation::getRoomType, Collectors.counting()));

        System.out.println("Bookings by Room Type: " + roomPopularity);
        System.out.println("========================================\n");
    }
}

// 4. Main Application Class
public class App {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        // Simulate successful confirmations
        System.out.println("System: Confirming and Archiving Bookings...");

        history.archiveBooking(new Reservation("RES-001", "Alice", "Deluxe", 250.0));
        history.archiveBooking(new Reservation("RES-002", "Bob", "Suite", 500.0));
        history.archiveBooking(new Reservation("RES-003", "Charlie", "Deluxe", 250.0));

        // Admin Review: Retrieve history
        System.out.println("\nAdmin: Reviewing Raw Audit Trail:");
        history.getAllRecords().forEach(System.out::println);

        // Admin Reporting: Generate Summary
        reportService.generateSummaryReport();
    }
}