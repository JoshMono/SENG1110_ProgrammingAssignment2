import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GymBranch {



    // TODO: Later check GymBranch and Member for any System.out or Scanner use.
    // Only GymSystemUI should handle input and output.




    private static final int MAX_MEMBERS = 15;

    private int branchId;
    private String branchName;

    private Member[] members;
    private int memberCount;

    private double totalRevenue;
    private int totalVisits;

    // Stores all available membership plan names
    private String[] planNames;

    // Stores how many members use each plan
    private int[] planCounts;

    private int shortDurationCount;
    private int midDurationCount;
    private int longDurationCount;

    private int trainerCount;
    private int lockerCount;


    // Creates a gym branch with its own member storage and statistics.
    public GymBranch(int branchId, String branchName, String[] planNames) {

        // Store branch information
        this.branchId = branchId;
        this.branchName = branchName;

        // Store available membership plans
        this.planNames = planNames;

        // Create member storage array
        members = new Member[MAX_MEMBERS];

        // Create plan counter array matching the number of plans
        planCounts = new int[planNames.length];

        // Initialise counters and totals
        memberCount = 0;

        totalRevenue = 0.0;
        totalVisits = 0;

        shortDurationCount = 0;
        midDurationCount = 0;
        longDurationCount = 0;

        trainerCount = 0;
        lockerCount = 0;
    }



    // Registers a new member to this branch.
    // If the entered member ID already exists, a new unique ID is generated.
    public String registerMember(int memberId, String name, String planName, double planPrice, int durationMonths, boolean hasTrainer, int trainerSessions, boolean hasLocker) {

        // Check if the branch member array is full
        if (isFull()) {
            return "Error: Branch is full. No more members can be registered.";
        }

        // Track whether the original ID had to be changed
        boolean idChanged = false;

        // Generate a new unique ID if the entered ID already exists
        if (isDuplicateMemberId(memberId)) {
            memberId = generateUniqueMemberId();
            idChanged = true;
        }

        // Create the new member
        Member newMember = new Member(
            memberId,
            name,
            planName,
            planPrice,
            durationMonths,
            hasTrainer,
            trainerSessions,
            hasLocker
        );

        // Add member to the array
        members[memberCount] = newMember;
        memberCount++;

        // Update branch statistics
        addCounters(newMember);

        // Return result message for the UI to display
        if (idChanged) {
            return "Member registered successfully.\nEntered ID was already used. New member ID assigned: " + memberId;
        }

        return "Member registered successfully. Member ID: " + memberId;
    }


    public String registerPromotionalMember(int memberId, String name, String planName, double planPrice, int durationMonths, boolean hasTrainer, int trainerSessions, boolean hasLocker) {

        if (isFull()) {
            return "Error: Branch is full. No more members can be registered.";
        }

        boolean idChanged = false;

        if (isDuplicateMemberId(memberId)) {
            memberId = generateUniqueMemberId();
            idChanged = true;
        }

        Member newMember = new Member(memberId, name, planName, planPrice,
                                    durationMonths, hasTrainer,
                                    trainerSessions, hasLocker);

        int promo = (int)(Math.random() * 3);
        String promoName = "";
        double discount = 0.0;

        if (promo == 0) {
            promoName = "First Month Free";

            if (durationMonths >= 2) {
                discount = planPrice;
            }
        }
        else if (promo == 1) {
            promoName = "20% Off Total";
            discount = newMember.getSubtotal() * 0.20;
        }
        else {
            promoName = "Free Locker for 3 Months";

            if (hasLocker) {
                if (durationMonths < 3) {
                    discount = 10.00 * durationMonths;
                }
                else {
                    discount = 10.00 * 3;
                }
            }
        }

        newMember.applyDiscount(discount);

        members[memberCount] = newMember;
        memberCount++;

        addCounters(newMember);

        String message = "Promotional member registered successfully.\n";
        message += "Promotion Applied: " + promoName + "\n";
        message += "Discount: $" + String.format("%.2f", discount) + "\n";
        message += "Final Total: $" + String.format("%.2f", newMember.getTotalCost());

        if (idChanged) {
            message += "\nEntered ID already existed. New member ID assigned: " + memberId;
        }

        return message;
    }


    // Records a gym visit for the most recently registered member.
    public String recordVisitForLastMember() {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "Error: No members are registered in this branch.";
        }

        // Get the most recently registered member
        Member lastMember = members[memberCount - 1];

        // Record visit for the member
        lastMember.recordVisit();

        // Update branch visit counter
        totalVisits++;

        // Return confirmation message for the UI
        return "Visit recorded for " + lastMember.getName()
            + ". Total visits: " + lastMember.getVisitCount();
    }



    // Modifies the most recently registered member's registration details.
    public String modifyLastRegistration(String newPlanName, double newPlanPrice, int newDurationMonths, boolean newHasTrainer, int newTrainerSessions, boolean newHasLocker) {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "Error: No members are registered in this branch.";
        }

        // Get the most recently registered member
        Member lastMember = members[memberCount - 1];

        // Remove the member's old statistics from branch totals
        removeCounters(lastMember);

        // Update the member registration details
        lastMember.updateRegistration(
            newPlanName,
            newPlanPrice,
            newDurationMonths,
            newHasTrainer,
            newTrainerSessions,
            newHasLocker
        );

        // Re-add updated member statistics
        addCounters(lastMember);

        // Return confirmation message for the UI
        return "Last member registration updated successfully.";
    }



    // Cancels and removes the most recently registered member.
    public String cancelLastRegistration() {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "Error: No members are registered in this branch.";
        }

        // Get the most recently registered member
        Member lastMember = members[memberCount - 1];

        // Remove this member's statistics from branch totals
        removeCounters(lastMember);

        // Remove member from the array
        members[memberCount - 1] = null;

        // Reduce member count
        memberCount--;

        // Return confirmation message for the UI
        return "Last registration cancelled for member ID: "
            + lastMember.getMemberId();
    }


    // Returns a summary of the most recently registered member.
    public String getLastMemberSummary() {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "No members are registered in this branch.";
        }

        // Get the most recently registered member
        Member lastMember = members[memberCount - 1];

        // Build and return the member summary
        return "Member ID: " + lastMember.getMemberId() + "\n"
            + "Name: " + lastMember.getName() + "\n"
            + "Plan: " + lastMember.getPlanName() + "\n"
            + "Duration: " + lastMember.getDurationMonths() + " months\n"
            + "Trainer: " + (lastMember.hasTrainer() ? "Yes" : "No") + "\n"
            + "Trainer Sessions: " + lastMember.getTrainerSessions() + "\n"
            + "Locker: " + (lastMember.hasLocker() ? "Yes" : "No") + "\n"
            + "Monthly Cost: $" + String.format("%.2f", lastMember.getMonthlyCost()) + "\n"
            + "Subtotal: $" + String.format("%.2f", lastMember.getSubtotal()) + "\n"
            + "GST: $" + String.format("%.2f", lastMember.getTax()) + "\n"
            + "Total Cost: $" + String.format("%.2f", lastMember.getTotalCost()) + "\n"
            + "Visits: " + lastMember.getVisitCount();
    }


    // Returns a formatted table of all registered members in this branch.
    public String getAllMembersTable() {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "No members are registered in this branch.";
        }

        // Store the table text
        String table = "ID | Name | Plan | Duration | Total Cost | Visits\n";
        table += "--------------------------------------------------\n";

        // Loop through all registered members
        for (int i = 0; i < memberCount; i++) {

            table += members[i].getMemberId() + " | "
                + members[i].getName() + " | "
                + members[i].getPlanName() + " | "
                + members[i].getDurationMonths() + " months | $"
                + String.format("%.2f", members[i].getTotalCost()) + " | "
                + members[i].getVisitCount() + "\n";
        }

        return table;
    }


    // Returns a summary of this branch's statistics.
    public String getBranchStatsSummary() {

        // Check if any members exist in this branch
        if (memberCount == 0) {
            return "No members are registered in this branch.";
        }

        // Store the branch statistics summary
        String summary = "========================================\n";
        summary += "           BRANCH STATISTICS\n";
        summary += "========================================\n";

        // Display branch details
        summary += "Branch ID: " + branchId + "\n";
        summary += "Branch Name: " + branchName + "\n";

        summary += "----------------------------------------\n";

        // Display member and visit statistics
        summary += "Total Members: " + memberCount + "\n";
        summary += "Total Visits: " + totalVisits + "\n";

        // Display revenue statistics
        summary += "Total Revenue: $"
                + String.format("%.2f", totalRevenue) + "\n";
        double averageValue = 0.0;

        if (memberCount > 0) {
            averageValue = totalRevenue / memberCount;
        }

        summary += "Average Registration Value: $"
                + String.format("%.2f", averageValue) + "\n";

        summary += "----------------------------------------\n";

        // Display duration statistics
        summary += "Short Duration Members: "
                + shortDurationCount + "\n";

        summary += "Mid Duration Members: "
                + midDurationCount + "\n";

        summary += "Long Duration Members: "
                + longDurationCount + "\n";

        summary += "----------------------------------------\n";

        // Display add-on statistics
        summary += "Members With Trainer: "
                + trainerCount + "\n";

        summary += "Members With Locker: "
                + lockerCount + "\n";

        summary += "----------------------------------------\n";

        // Display dynamic membership plan statistics
        for (int i = 0; i < planNames.length; i++) {

            summary += planNames[i] + " Members: "
                    + planCounts[i] + "\n";
        }

        summary += "========================================";

        return summary;
    }



    // Recalculates all branch statistics from the loaded member array.
    public void recalculateStatsFromMembers() {

        totalRevenue = 0.0;
        totalVisits = 0;

        shortDurationCount = 0;
        midDurationCount = 0;
        longDurationCount = 0;

        trainerCount = 0;
        lockerCount = 0;

        for (int i = 0; i < planCounts.length; i++) {
            planCounts[i] = 0;
        }

        for (int i = 0; i < memberCount; i++) {
            addCounters(members[i]);
        }
    }

    // Adds this member's details to the branch statistics.
    private void addCounters(Member member) {

        // Add member cost to total revenue
        totalRevenue += member.getTotalCost();

        // Add member visits to total visits
        totalVisits += member.getVisitCount();

        // Update duration category counters
        if (member.getDurationMonths() <= 3) {
            shortDurationCount++;
        }
        else if (member.getDurationMonths() <= 6) {
            midDurationCount++;
        }
        else {
            longDurationCount++;
        }

        // Update add-on counters
        if (member.hasTrainer()) {
            trainerCount++;
        }

        if (member.hasLocker()) {
            lockerCount++;
        }

        // Update dynamic plan counter
        for (int i = 0; i < planNames.length; i++) {

            if (planNames[i].equalsIgnoreCase(member.getPlanName())) {
                planCounts[i]++;
                return;
            }
        }
    }


    // Removes this member's details from the branch statistics.
    private void removeCounters(Member member) {

        // Remove member cost from total revenue
        totalRevenue -= member.getTotalCost();

        // Remove member visits from total visits
        totalVisits -= member.getVisitCount();

        // Update duration category counters
        if (member.getDurationMonths() <= 3) {
            shortDurationCount--;
        }
        else if (member.getDurationMonths() <= 6) {
            midDurationCount--;
        }
        else {
            longDurationCount--;
        }

        // Update add-on counters
        if (member.hasTrainer()) {
            trainerCount--;
        }

        if (member.hasLocker()) {
            lockerCount--;
        }

        // Update dynamic plan counter
        for (int i = 0; i < planNames.length; i++) {

            if (planNames[i].equalsIgnoreCase(member.getPlanName())) {
                planCounts[i]--;
                return;
            }
        }
    }


    // // Loads branch data from a file and returns the created branch.
    // public static GymBranch loadBranchData(String filename, String[] planNames, double[] planPrices) {

    //     try {

    //         Scanner fileScanner = new Scanner(new File(filename));

    //         // Read branch statistics line
    //         String branchLine = fileScanner.nextLine();

    //         String[] branchParts = branchLine.split(",");

    //         int branchId = Integer.parseInt(branchParts[0].trim());
    //         String branchName = branchParts[1].trim();

    //         // Create branch
    //         GymBranch loadedBranch = new GymBranch(branchId, branchName, planNames);

    //         // Read member lines
    //         while (fileScanner.hasNextLine()) {

    //             String memberLine =
    //                 fileScanner.nextLine().trim();

    //             // Skip blank lines
    //             if (memberLine.isEmpty()) {
    //                 continue;
    //             }

    //             String[] memberParts =
    //                 memberLine.split(",");

    //             int memberId =
    //                 Integer.parseInt(memberParts[0].trim());

    //             String name =
    //                 memberParts[1].trim();

    //             String planName =
    //                 memberParts[2].trim();

    //             int durationMonths =
    //                 Integer.parseInt(memberParts[3].trim());

    //             boolean hasTrainer =
    //                 Boolean.parseBoolean(memberParts[4].trim());

    //             int trainerSessions =
    //                 Integer.parseInt(memberParts[5].trim());

    //             boolean hasLocker =
    //                 Boolean.parseBoolean(memberParts[6].trim());

    //             // Find matching plan price

    //             double planPrice = getPlanPriceFromArray(planName, planNames, planPrices);

    //             if (planPrice == -1) {
    //                 fileScanner.close();
    //                 return null;
    //             }

    //             loadedBranch.registerMember(
    //                 memberId,
    //                 name,
    //                 planName,
    //                 planPrice,
    //                 durationMonths,
    //                 hasTrainer,
    //                 trainerSessions,
    //                 hasLocker
    //             );
    //         }

    //         fileScanner.close();

    //         return loadedBranch;
    //     }
    //     catch (Exception e) {
    //         return null;
    //     }
    // }



    private static boolean parseBoolean(String input) throws Exception {
        if (input.equalsIgnoreCase("true")) {
            return true;
        }

        if (input.equalsIgnoreCase("false")) {
            return false;
        }

        throw new Exception();
    }


    public static int loadAllBranches(String filename, GymBranch[] branches, String[] planNames, double[] planPrices) {

        try {
            Scanner fileScanner = new Scanner(new File(filename));

            int branchCount = 0;
            GymBranch currentBranch = null;

            int branchFieldCount = 5 + planNames.length + 5;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length == branchFieldCount) {

                    if (branchCount >= branches.length) {
                        currentBranch = null;
                        continue;
                    }

                    int branchId = Integer.parseInt(parts[0].trim());
                    String branchName = parts[1].trim();

                    if (branchName.isEmpty()) {
                        continue;
                    }

                    currentBranch = new GymBranch(branchId, branchName, planNames);

                    branches[branchCount] = currentBranch;
                    branchCount++;
                }
                else if (parts.length == 12) {

                    if (currentBranch == null) {
                        continue;
                    }

                    int memberId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String planName = parts[2].trim();
                    int durationMonths = Integer.parseInt(parts[3].trim());

                    boolean hasTrainer = parseBoolean(parts[4].trim());
                    int trainerSessions = Integer.parseInt(parts[5].trim());
                    boolean hasLocker = parseBoolean(parts[6].trim());

                    double savedAddOns = Double.parseDouble(parts[7].trim());
                    double savedSubtotal = Double.parseDouble(parts[8].trim());
                    double savedTax = Double.parseDouble(parts[9].trim());
                    double savedTotal = Double.parseDouble(parts[10].trim());
                    int savedVisits = Integer.parseInt(parts[11].trim());

                    double planPrice = getPlanPriceFromArray(planName, planNames, planPrices);
                                        
                    if (planPrice < 0) {
                        continue;
                    }

                    if (name.isEmpty() || durationMonths < 1 || durationMonths > 12) {
                        continue;
                    }

                    if (trainerSessions < 0 || trainerSessions > 4 || savedVisits < 0) {
                        continue;
                    }

                    if (savedAddOns < 0 || savedSubtotal < 0 || savedTax < 0 || savedTotal < 0) {
                        continue;
                    }

                    boolean added = currentBranch.addLoadedMember(
                        memberId,
                        name,
                        planName,
                        planPrice,
                        durationMonths,
                        hasTrainer,
                        trainerSessions,
                        hasLocker,
                        savedSubtotal,
                        savedTax,
                        savedTotal,
                        savedVisits
                    );

                    if (!added) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }

            fileScanner.close();
            for (int i = 0; i < branchCount; i++) {
                branches[i].recalculateStatsFromMembers();
            }
            return branchCount;
        }
        catch (Exception e) {
            return -1;
        }
    }




    // Returns all branch and member data formatted for file saving.
    public String toFileString() {

        String data = "";

        data += branchId + ","
            + branchName + ","
            + String.format("%.2f", totalRevenue) + ","
            + memberCount + ","
            + totalVisits + ",";

        for (int i = 0; i < planCounts.length; i++) {
            data += planCounts[i] + ",";
        }

        data += shortDurationCount + ","
            + midDurationCount + ","
            + longDurationCount + ","
            + trainerCount + ","
            + lockerCount + "\n";

        for (int i = 0; i < memberCount; i++) {
            data += members[i].toFileString() + "\n";
        }

        return data;
    }


    // Saves all branches and their members to one text file.
    public static String saveAllBranches(String filename, GymBranch[] branches, int branchCount) {

        try {
            PrintWriter writer = new PrintWriter(filename);

            for (int i = 0; i < branchCount; i++) {
                writer.print(branches[i].toFileString());
            }

            writer.close();

            return "Branch data saved successfully.";
        }
        catch (Exception e) {
            return "Error: Could not save branch data.";
        }
    }



    // Adds a member from file data into this branch.
    public boolean addLoadedMember(int memberId, String name, String planName, double planPrice, int durationMonths, boolean hasTrainer, int trainerSessions,
                               boolean hasLocker, double savedSubtotal, double savedTax, double savedTotal, int savedVisits) {

        if (isFull()) {
            return false;
        }

        if (isDuplicateMemberId(memberId)) {
            return false;
        }

        Member member = new Member(
            memberId,
            name,
            planName,
            planPrice,
            durationMonths,
            hasTrainer,
            trainerSessions,
            hasLocker
        );

        member.loadSavedData(savedSubtotal, savedTax, savedTotal, savedVisits);

        members[memberCount] = member;
        memberCount++;

        addCounters(member);

        return true;
    }


    // Finds the price of a plan from the loaded plans array.
    private static double getPlanPriceFromArray(String planName, String[] planNames, double[] planPrices) {

        for (int i = 0; i < planNames.length; i++) {
            if (planNames[i].equalsIgnoreCase(planName)) {
                return planPrices[i];
            }
        }

        return -1;
    }


    // Generates a unique member ID for this branch.
    private int generateUniqueMemberId() {

        // Starting member ID number
        int memberId = 1000;

        // Continue searching until a unique ID is found
        while (isDuplicateMemberId(memberId)) {
            memberId++;
        }

        // Return the unique member ID
        return memberId;
    }



    //
    //
    // Validation
    //
    //


    // Checks whether a member ID already exists in this branch.
    private boolean isDuplicateMemberId(int memberId) {

        for (int i = 0; i < memberCount; i++) {

            if (members[i].getMemberId() == memberId) {
                return true;
            }
        }

        return false;
    }



    // Checks whether the branch has reached the member limit.
    public boolean isFull() {
        return memberCount >= MAX_MEMBERS;
    }


    //
    //
    // Getters only
    //
    //
    
    
    // Returns the branch ID.
    public int getBranchId() {
        return branchId;
    }


    // Returns the branch name.
    public String getBranchName() {
        return branchName;
    }


    // Returns the number of registered members.
    public int getMemberCount() {
        return memberCount;
    }

    public String getLastMemberPlanName() {
        return members[memberCount - 1].getPlanName();
    }

    public int getLastMemberDuration() {
        return members[memberCount - 1].getDurationMonths();
    }

    public boolean getLastMemberHasTrainer() {
        return members[memberCount - 1].hasTrainer();
    }

    public int getLastMemberTrainerSessions() {
        return members[memberCount - 1].getTrainerSessions();
    }

    public boolean getLastMemberHasLocker() {
        return members[memberCount - 1].hasLocker();
    }

}