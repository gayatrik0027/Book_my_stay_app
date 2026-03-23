import java.util.*;

// 1. Custom Exceptions for domain-specific errors
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

// 2. Validator: The gatekeeper for system state
class BookingValidator {
    private static final List<String> VALID_ROOM_TYPES = Arrays.asList("SINGLE", "DOUBLE", "SUITE");

    public void validateRequest(String roomType, int requestedRooms, int availableRooms)
            throws InvalidBookingException, InsufficientInventoryException {

        // Input Validation: Check if room type exists
        if (!VALID_ROOM_TYPES.contains(roomType.toUpperCase())) {
            throw new InvalidBookingException("Error: Room type '" + roomType + "' is not supported.");
        }

        // Input Validation: Check for logical numbers
        if (requestedRooms <= 0) {
            throw new InvalidBookingException("Error: Number of rooms must be greater than zero.");
        }

        // Guarding System State: Prevent negative inventory
        if (requestedRooms > availableRooms) {
            throw new InsufficientInventoryException("Error: Only " + availableRooms + " rooms left for " + roomType);
        }
    }
}

// 3. Main Application Class
public class App {
    private static int availableSuites = 2; // Mock inventory

    public static void main(String[] args) {
        BookingValidator validator = new BookingValidator();

        // Scenario A: Invalid Room Type
        processBooking(validator, "PENTHOUSE", 1);

        // Scenario B: Invalid Quantity
        processBooking(validator, "SUITE", -5);

        // Scenario C: Exceeding Inventory
        processBooking(validator, "SUITE", 5);

        // Scenario D: Happy Path (Correctness)
        processBooking(validator, "SUITE", 1);
    }

    private static void processBooking(BookingValidator validator, String type, int qty) {
        System.out.println("Attempting to book " + qty + " " + type + "(s)...");

        try {
            // Fail-Fast: Check everything before updating state
            validator.validateRequest(type, qty, availableSuites);

            // If we reach here, validation passed
            availableSuites -= qty;
            System.out.println("SUCCESS: Booking confirmed. Remaining Suites: " + availableSuites);

        } catch (InvalidBookingException | InsufficientInventoryException e) {
            // Graceful Failure Handling: Catch and display meaningful messages
            System.err.println("VALIDATION FAILED: " + e.getMessage());
        } catch (Exception e) {
            // Safety net for unexpected errors
            System.err.println("SYSTEM ERROR: An unexpected error occurred.");
        }
        System.out.println("------------------------------------------------");
    }
}