
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
            + "Trainer: " + lastMember.hasTrainer() + "\n"
            + "Trainer Sessions: " + lastMember.getTrainerSessions() + "\n"
            + "Locker: " + lastMember.hasLocker() + "\n"
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

    // public String saveBranchData(String filename)

    // public static GymBranch loadBranchData(String filename)




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

    // private void recalculateStatsFromMembers()

    // private boolean isValidMemberData(...)

    // private boolean isValidBranchData(...)



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

}