// Stores one gym member and calculates that member's membership costs.
public class Member {

    // Fixed prices used when calculating membership costs.
    private static final double TRAINER_PRICE = 25.00;
    private static final double LOCKER_PRICE = 10.00;
    private static final double GST_RATE = 0.10;

    // Basic member registration details.
    private int memberId;
    private String name;
    private String planName;
    private double planPrice;
    private int durationMonths;

    // Optional add-on selections.
    private boolean hasTrainer;
    private int trainerSessions;
    private boolean hasLocker;

    // Calculated membership costs.
    private double monthlyCost;
    private double subtotal;
    private double tax;
    private double totalCost;

    // Number of visits recorded for this member.
    private int visitCount;

    // Creates a member and immediately calculates their membership costs.
    public Member(int memberId, String name, String planName, double planPrice,
                  int durationMonths, boolean hasTrainer, int trainerSessions,
                  boolean hasLocker) {

        this.memberId = memberId;
        this.name = name;
        this.planName = planName;
        this.planPrice = planPrice;
        this.durationMonths = durationMonths;
        this.hasTrainer = hasTrainer;
        this.trainerSessions = trainerSessions;
        this.hasLocker = hasLocker;

        visitCount = 0;

        calculateCosts();
    }
    

    // Calculates all membership costs for the member.
    private void calculateCosts() {

        // Start with the selected membership plan price
        monthlyCost = planPrice;

        // Add trainer session costs if selected
        if (hasTrainer) {
            monthlyCost += trainerSessions * TRAINER_PRICE;
        }

        // Add locker cost if selected
        if (hasLocker) {
            monthlyCost += LOCKER_PRICE;
        }

        // Calculate subtotal across the full membership duration
        subtotal = monthlyCost * durationMonths;

        // Calculate GST amount
        tax = subtotal * GST_RATE;

        // Calculate final total cost including GST
        totalCost = subtotal + tax;
    }


    // Records a gym visit for the member.
    public void recordVisit() {
        visitCount++;
    }

    // Calculates and returns the total add-on cost for the member.
    // Includes trainer session costs and locker costs if selected.
    public double getAddOnsCost() {

        // Store the calculated add-on total
        double addOnsCost = 0.0;

        // Add trainer session costs if selected
        if (hasTrainer) {
            addOnsCost += trainerSessions * TRAINER_PRICE;
        }

        // Add locker cost if selected
        if (hasLocker) {
            addOnsCost += LOCKER_PRICE;
        }

        // Return the final add-on cost
        return addOnsCost;
    }


    // Updates the member's registration details,
    // then recalculates all membership costs.
    public void updateRegistration(String newPlanName, double newPlanPrice, int newDurationMonths, boolean newHasTrainer, int newTrainerSessions, boolean newHasLocker) {

        // Update plan information
        planName = newPlanName;
        planPrice = newPlanPrice;

        // Update duration
        durationMonths = newDurationMonths;

        // Update add-on details
        hasTrainer = newHasTrainer;
        trainerSessions = newTrainerSessions;
        hasLocker = newHasLocker;

        // Recalculate all membership costs after changes
        calculateCosts();
    }


    // Returns member data formatted for file saving.
    public String toFileString() {
        return memberId + ","
            + name + ","
            + planName + ","
            + durationMonths + ","
            + hasTrainer + ","
            + trainerSessions + ","
            + hasLocker + ","
            + String.format("%.2f", getAddOnsCost()) + ","
            + String.format("%.2f", subtotal) + ","
            + String.format("%.2f", tax) + ","
            + String.format("%.2f", totalCost) + ","
            + visitCount;
    }


    // Applies a promotional discount before GST is calculated.
    public void applyDiscount(double discount) {

        subtotal -= discount;

        if (subtotal < 0) {
            subtotal = 0;
        }

        tax = subtotal * GST_RATE;
        totalCost = subtotal + tax;
    }


    // Restores saved cost and visit values when a member is loaded from file.
    public void loadSavedData(double subtotal, double tax, double totalCost, int visitCount) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.totalCost = totalCost;
        this.visitCount = visitCount;
    }
    

    
    //
    //
    // Getters only
    //
    //


    // Returns the member ID.
    public int getMemberId() {
        return memberId;
    }


    // Returns the member name.
    public String getName() {
        return name;
    }


    // Returns the membership plan name.
    public String getPlanName() {
        return planName;
    }


    // Returns the monthly membership plan price.
    public double getPlanPrice() {
        return planPrice;
    }


    // Returns the membership duration in months.
    public int getDurationMonths() {
        return durationMonths;
    }


    // Returns whether the member has a trainer package.
    public boolean hasTrainer() {
        return hasTrainer;
    }


    // Returns the number of trainer sessions selected.
    public int getTrainerSessions() {
        return trainerSessions;
    }


    // Returns whether the member has a locker included.
    public boolean hasLocker() {
        return hasLocker;
    }


    // Returns the monthly membership cost.
    public double getMonthlyCost() {
        return monthlyCost;
    }


    // Returns the subtotal before GST.
    public double getSubtotal() {
        return subtotal;
    }


    // Returns the GST amount.
    public double getTax() {
        return tax;
    }


    // Returns the final total membership cost.
    public double getTotalCost() {
        return totalCost;
    }


    // Returns the total number of recorded visits.
    public int getVisitCount() {
        return visitCount;
    }
}
