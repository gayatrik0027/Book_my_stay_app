import java.util.*;

// 1. Policy Interface: The contract for all business rules
interface BookingPolicy {
    boolean isAllowed(String guestType, int duration, double totalCost);
    String getFailureMessage();
}

// 2. Concrete Policy: Minimum Stay Rule
class MinimumStayPolicy implements BookingPolicy {
    private final int minDays = 2;

    @Override
    public boolean isAllowed(String guestType, int duration, double totalCost) {
        return duration >= minDays;
    }

    @Override
    public String getFailureMessage() {
        return "Business Rule Violation: Minimum stay is " + minDays + " nights.";
    }
}

// 3. Concrete Policy: VIP Credit Check
class CreditLimitPolicy implements BookingPolicy {
    private final double maxCredit = 1000.0;

    @Override
    public boolean isAllowed(String guestType, int duration, double totalCost) {
        // Standard guests cannot book over the credit limit
        if (guestType.equalsIgnoreCase("STANDARD") && totalCost > maxCredit) {
            return false;
        }
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Business Rule Violation: Cost exceeds credit limit for Standard guests.";
    }
}

// 4. BRP Manager: Evaluates all active policies
class BusinessRuleProcessor {
    private final List<BookingPolicy> policies = new ArrayList<>();

    public void addPolicy(BookingPolicy policy) {
        policies.add(policy);
    }

    public boolean validatePolicies(String guestType, int duration, double cost) {
        for (BookingPolicy policy : policies) {
            if (!policy.isAllowed(guestType, duration, cost)) {
                System.err.println(policy.getFailureMessage());
                return false; // Fail-Fast on first policy violation
            }
        }
        return true;
    }
}

// 5. Main Application Class
public class App {
    public static void main(String[] args) {
        BusinessRuleProcessor brp = new BusinessRuleProcessor();

        // Registering Business Rules
        brp.addPolicy(new MinimumStayPolicy());
        brp.addPolicy(new CreditLimitPolicy());

        System.out.println("--- Scenario 1: Short Stay (1 Night) ---");
        boolean canBook1 = brp.validatePolicies("STANDARD", 1, 150.0);
        System.out.println("Result: " + (canBook1 ? "Approved" : "Rejected"));

        System.out.println("\n--- Scenario 2: High Cost Standard Guest ---");
        boolean canBook2 = brp.validatePolicies("STANDARD", 3, 1200.0);
        System.out.println("Result: " + (canBook2 ? "Approved" : "Rejected"));

        System.out.println("\n--- Scenario 3: VIP Guest High Cost ---");
        boolean canBook3 = brp.validatePolicies("VIP", 3, 1500.0);
        System.out.println("Result: " + (canBook3 ? "Approved" : "Rejected"));
    }
}