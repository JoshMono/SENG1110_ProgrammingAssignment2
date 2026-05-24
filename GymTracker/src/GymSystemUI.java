/*
 * Author: Joshua Monaghan
 * Student ID: c3580928
 * Course: SENG1110
 * Assessment: Assessment 2
 * Description: 
 * Console-based gym membership management system
 * that allows users to manage branches, register
 * members, record visits, modify registrations,
 * compare plans, simulate promotions, and save/load
 * branch data from files.
 * Date Created: 18/05/2026
 * Last Modified: 24/05/2026
 */


// Handles all user input/output, menus, branch selection, and overall program state.
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GymSystemUI {

    // Scanner used for all keyboard input.
    private Scanner scanner;

    // Stores up to three gym branches and tracks the active branch.
    private GymBranch[] branches;
    private int branchCount;
    private int activeBranchIndex;

    // Membership plan data loaded from plans.txt or defaults.
    private String[] planNames;
    private double[] planPrices;
    private String[] planFeatures;
    private int planCount;

    // Program entry point.
    public static void main(String[] args) throws Exception {
        GymSystemUI ui = new GymSystemUI();
        ui.run();
    }

    // Creates the UI object and prepares branch storage.
    public GymSystemUI() {
        scanner = new Scanner(System.in);

        // Assignment allows a maximum of three branches.
        branches = new GymBranch[3];

        branchCount = 0;
        activeBranchIndex = -1;
        
    }
    
    // Starts the program, loads plans, creates a default branch, and runs the main menu loop.
    private void run() {
        scanner = new Scanner(System.in);

        loadPlans();

        if (branchCount == 0) {
            branches[0] = new GymBranch(1000, "Default Branch", planNames);
            branchCount = 1;
            activeBranchIndex = 0;
        }

        boolean running = true;

        while (running) {
            clear();
            displayMainMenu();

            int choice = getIntInRange("Enter choice: ", 1, 13);

            if (choice == 13) {
                running = false;
            }
            else {
                handleMenuChoice(choice);
            }
        }

        System.out.println("Goodbye.");
    }

    // Displays the main menu and the currently active branch.
    private void displayMainMenu() {
        System.out.println("========================================");
        System.out.println("        GYM MEMBERSHIP SYSTEM");
        System.out.println("========================================");
        
        if (activeBranch() == null) {
            System.out.println("Active Branch: None");
        }
        else {
            System.out.println(
                "Active Branch: "
                + activeBranch().getBranchId()
                + " - "
                + activeBranch().getBranchName()
            );
        }

        System.out.println("========================================");

        System.out.println("1.  Manage Branches");
        System.out.println("2.  View Membership Plans");
        System.out.println("3.  Register New Member");
        System.out.println("4.  Record a Gym Visit");
        System.out.println("5.  View Member Summary");
        System.out.println("6.  Compare Two Membership Plans");
        System.out.println("7.  Simulate Promotional Offer");
        System.out.println("8.  Modify Last Registration");
        System.out.println("9.  Cancel Last Registration");
        System.out.println("10. View All Registered Members");
        System.out.println("11. Save Branch Data");
        System.out.println("12. Load Branch Data");
        System.out.println("13. Exit");

        System.out.println("========================================");
    }

    // Handles the selected main menu option.
    private void handleMenuChoice(int choice) {

        switch (choice) {

            // Manage branches menu
            case 1:
                manageBranches();
                break;

            // View all membership plans
            case 2:
                viewMembershipPlans();
                break;

            // Register a new member
            case 3:
                registerNewMember();
                break;

            // Record a gym visit
            case 4:
                recordGymVisit();
                break;

            // View member and branch summaries
            case 5:
                viewMemberSummary();
                break;

            // Compare membership plans
            case 6:
                compareMembershipPlans();
                break;

            // Simulate promotional offer
            case 7:
                simulatePromotionalOffer();
                break;

            // Modify the last registration
            case 8:
                modifyLastRegistration();
                break;

            // Cancel the last registration
            case 9:
                cancelLastRegistration();
                break;

            // View all registered members
            case 10:
                viewAllRegisteredMembers();
                break;

            // Save branch data
            case 11:
                saveBranchData();
                break;

            // Load branch data
            case 12:
                loadBranchData();
                break;
        }
    }


    //
    //
    // Branch UI
    //
    //


   // Displays the branch management menu and handles branch actions.
    private void manageBranches() {

        // Controls the branch management loop
        boolean managing = true;

        // Continue showing the menu until the user chooses to return
        while (managing) {

            // Clear the console before displaying the menu
            clear();

            // Display menu heading
            System.out.println("========================================");
            System.out.println("           MANAGE BRANCHES");
            System.out.println("========================================");

            // Display branch management options
            System.out.println("1. Create Branch");
            System.out.println("2. Select Active Branch");
            System.out.println("3. View Branches");
            System.out.println("4. Return");

            System.out.println("========================================");

            // Get validated menu choice from the user
            int choice = getIntInRange("Enter choice: ", 1, 4);

            // Handle selected menu option
            switch (choice) {

                // Create a new branch
                case 1:
                    createBranch();
                    break;

                // Select the active branch
                case 2:
                    selectBranch();
                    break;

                // View all created branches
                case 3:
                    viewBranches();
                    break;

                // Return to the main menu
                case 4:
                    managing = false;
                    break;
            }
        }
    }

    // Checks whether a branch ID already exists.
    private boolean isDuplicateBranchId(int branchId) {

        // Loop through all existing branches
        for (int i = 0; i < branchCount; i++) {

            // Check if the branch ID already exists
            if (branches[i].getBranchId() == branchId) {
                return true;
            }
        }

        // Branch ID is unique
        return false;
    }

    // Generates a random unique branch ID.
    private int generateUniqueBranchId() {

        int newId = 1000;

        // Continue generating IDs until a unique ID is found
        while (isDuplicateBranchId(newId)) {
            newId++;
        }

        // Return the unique branch ID
        return newId;
    }


    // Creates a new gym branch.
    private void createBranch() {

        clear();

        System.out.println("========================================");
        System.out.println("           CREATE BRANCH");
        System.out.println("========================================");

        // Check if the maximum number of branches has been reached
        if (branchCount >= branches.length) {

            System.out.println("Error: Maximum number of branches reached.");
            pause();
            return;
        }

        // Get branch details from the user
        int branchId = getIntInRange("Enter branch ID: ", 1, 999999);

        String branchName = getNonEmptyString("Enter branch name: ");

        // Track whether the ID had to be changed
        boolean idChanged = false;

        // Generate a new ID if the entered ID already exists
        if (isDuplicateBranchId(branchId)) {

            branchId = generateUniqueBranchId();

            idChanged = true;
        }

        // Create the new branch
        branches[branchCount] =
            new GymBranch(branchId, branchName, planNames);

        // Set the new branch as active
        activeBranchIndex = branchCount;

        // Increase total branch count
        branchCount++;

        // Display success message
        System.out.println("Branch created successfully.");

        // Display generated ID message if needed
        if (idChanged) {

            System.out.println(
                "Entered branch ID already existed."
            );

            System.out.println(
                "New branch ID assigned: " + branchId
            );
        }

        // Display active branch information
        System.out.println(
            "Active branch set to: " + branchName
        );

        pause();
    }

    // Displays all created branches.
    private void viewBranches() {

        // Clear the console before displaying branches
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("             BRANCHES");
        System.out.println("========================================");

        // Check if any branches exist
        if (branchCount == 0) {

            // Display message if no branches exist
            System.out.println("No branches have been created.");

            // Pause before returning
            pause();
            return;
        }

        // Loop through all branches
        for (int i = 0; i < branchCount; i++) {

            // Display active branch indicator
            if (i == activeBranchIndex) {
                System.out.print("[ACTIVE] ");
            }

            // Display branch details
            System.out.println(
                branches[i].getBranchId()
                + " - "
                + branches[i].getBranchName()
            );
        }

        // Pause before returning
        pause();
    }

    // Allows the user to select the active branch.
    private void selectBranch() {

        // Clear the console before displaying the menu
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("            SELECT BRANCH");
        System.out.println("========================================");

        // Check if any branches exist
        if (branchCount == 0) {

            // Display error message
            System.out.println("Error: No branches available.");

            // Pause before returning
            pause();
            return;
        }

        // Get branch ID from the user
        int branchId = getIntInRange("Enter branch ID: ", 1, 999999);

        // Search for the matching branch
        for (int i = 0; i < branchCount; i++) {

            // Check if the branch ID matches
            if (branches[i].getBranchId() == branchId) {

                // Set the selected branch as active
                activeBranchIndex = i;

                // Display success message
                System.out.println(
                    "Active branch changed to: "
                    + branches[i].getBranchName()
                );

                // Pause before returning
                pause();
                return;
            }
        }

        // Display error if branch ID was not found
        System.out.println("Error: Branch ID not found.");

        // Pause before returning
        pause();
    }

    // Returns the currently active branch.
    // Returns null if no active branch exists.
    private GymBranch activeBranch() {

        // Check if an active branch has been selected
        if (activeBranchIndex == -1) {
            return null;
        }

        // Return the active branch
        return branches[activeBranchIndex];
    }


    //
    //
    // Member UI
    //
    //


    // Displays all loaded membership plans and their details.
    private void viewMembershipPlans() {

        // Clear the console before displaying plan information
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("         MEMBERSHIP PLANS");
        System.out.println("========================================");

        // Check if any plans are loaded
        if (planCount == 0) {

            // Display message if no plans exist
            System.out.println("No Membership Plans Available.");

            // Pause before returning to the menu
            pause();
            return;
        }

        // Loop through all loaded plans
        for (int i = 0; i < planCount; i++) {

            // Display plan number
            System.out.println("Plan " + (i + 1));

            // Display plan name
            System.out.println("Name: " + planNames[i]);

            // Display formatted monthly plan price
            System.out.println(
                "Price: $" + String.format("%.2f", planPrices[i])
            );

            // Display plan features
            System.out.println("Features: " + planFeatures[i]);

            // Display section divider
            System.out.println("----------------------------------------");
        }
        System.out.println("Add-Ons:");
        System.out.println("Personal Trainer Session: $25.00 per session/month");
        System.out.println("Locker Rental: $10.00 per month");

        // Pause before returning to the menu
        pause();
    }


    // Loads membership plans from the plans.txt configuration file.
    // Invalid plans are skipped and default plans are loaded if no valid plans exist.
    private void loadPlans() {

        // Reset the total number of loaded plans
        planCount = 0;

        try {

            // Open the plans configuration file
            File file = new File("plans.txt");

            // Scanner used to count valid plan lines
            Scanner countScanner = new Scanner(file);

            // Count the number of valid plans in the file
            while (countScanner.hasNextLine()) {

                // Read and trim the current line
                String line = countScanner.nextLine().trim();

                // Ignore blank lines
                if (!line.isEmpty()) {

                    // Check whether the plan line is valid
                    if (isValidPlanLine(line)) {

                        // Increase valid plan count
                        planCount++;
                    }
                    else {

                        // Display warning for invalid plan lines
                        System.out.println(
                            "Warning: Invalid plan line skipped: " + line
                        );
                    }
                }
            }

            // Close the counting scanner
            countScanner.close();

            // Load default plans if no valid plans were found
            if (planCount == 0) {

                System.out.println(
                    "Error: No valid plans found. Loading defaults."
                );

                loadDefaultPlans();
                return;
            }

            // Create arrays using the number of valid plans found
            planNames = new String[planCount];
            planPrices = new double[planCount];
            planFeatures = new String[planCount];

            // Scanner used to load actual plan data
            Scanner fileScanner = new Scanner(file);

            int index = 0;

            // Read and store each valid plan
            while (fileScanner.hasNextLine()) {

                // Read and trim the current line
                String line = fileScanner.nextLine().trim();

                // Skip blank or invalid lines
                if (line.isEmpty() || !isValidPlanLine(line)) {
                    continue;
                }

                // Split line into sections using commas
                String[] parts = line.split(",");

                // Remove extra whitespace from each value
                String name = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());
                String features = parts[2].trim();

                // Store plan data into arrays
                planNames[index] = name;
                planPrices[index] = price;
                planFeatures[index] = features;

                // Move to the next array position
                index++;
            }

            // Close the file scanner
            fileScanner.close();

            // Display success message
            System.out.println("Plans Loaded Successfully.");
        }

        catch (Exception e) {

            System.out.println("plans.txt not found.");
            System.out.println("Creating default plans file...");

            // Default plans
            loadDefaultPlans();

            planCount = 3;

            // Create new plans.txt file
            try {

                PrintWriter writer = new PrintWriter("plans.txt");

                for (int i = 0; i < planCount; i++) {

                    writer.println(
                        planNames[i] + ","
                        + planPrices[i] + ","
                        + planFeatures[i]
                    );
                }

                writer.close();

                System.out.println("Default plans.txt created successfully.");
            }
            
            catch (Exception fileError) {

                System.out.println("Error: Could not create default plans file.");
            }
        }
    }


    // Checks whether a line from plans.txt contains valid plan data.
    private boolean isValidPlanLine(String line) {

        // Split the line into comma-separated sections
        String[] parts = line.split(",");

        // Check the correct number of fields exists
        if (parts.length != 3) {
            return false;
        }

        // Remove extra whitespace from each value
        String name = parts[0].trim();
        String priceText = parts[1].trim();
        String features = parts[2].trim();

        // Ensure required text fields are not blank
        if (name.isEmpty() || features.isEmpty()) {
            return false;
        }

        try {

            // Attempt to convert the price into a double
            double price = Double.parseDouble(priceText);

            // Ensure the plan price is greater than zero
            if (price <= 0) {
                return false;
            }
        }

        // Handles invalid number formatting
        catch (NumberFormatException e) {
            return false;
        }

        // Plan line is valid
        return true;
    }


    // Loads fallback default membership plans.
    // Used when plans.txt cannot be loaded or contains no valid plans.
    private void loadDefaultPlans() {

        // Set the total number of default plans
        planCount = 3;

        // Create arrays for default plan data
        planNames = new String[planCount];
        planPrices = new double[planCount];
        planFeatures = new String[planCount];

        // Store Basic plan data
        planNames[0] = "Basic";
        planPrices[0] = 29.99;
        planFeatures[0] = "Gym floor access only";

        // Store Standard plan data
        planNames[1] = "Standard";
        planPrices[1] = 49.99;
        planFeatures[1] = "Gym floor + group classes";

        // Store Premium plan data
        planNames[2] = "Premium";
        planPrices[2] = 79.99;
        planFeatures[2] = "Gym floor + classes + pool + sauna";
    }   


    // Collects registration details from the user and adds a member to the active branch.
    private void registerNewMember() {
        clear();

        System.out.println("========================================");
        System.out.println("        REGISTER NEW MEMBER");
        System.out.println("========================================");

        if (activeBranch() == null) {
            System.out.println("Error: No active branch selected.");
            pause();
            return;
        }

        // Get basic member details.
        int memberId = getIntInRange("Enter member ID: ", 1, 999999);
        String name = getNonEmptyString("Enter member name: ");

        // Get and price the selected membership plan.
        String planName = getPlanType("Enter membership plan: ");
        
        double planPrice = 0;

        for (int i = 0; i < planCount; i++) {

            if (planNames[i].equalsIgnoreCase(planName)) {
                planPrice = planPrices[i];
                break;
            }
        }

        // Get membership duration and optional add-ons.
        int duration = getIntInRange("Enter duration in months: ", 1, 12);

        boolean hasTrainer = getYesNo("Add personal trainer sessions? (Y/N): ");
        int trainerSessions = 0;

        if (hasTrainer) {
            trainerSessions = getIntInRange("Enter trainer sessions per month (1-4): ", 1, 4);
        }

        boolean hasLocker = getYesNo("Add locker rental? (Y/N): ");

        // Register the member through the active branch.
        String result = activeBranch().registerMember(
            memberId,
            name,
            planName,
            planPrice,
            duration,
            hasTrainer,
            trainerSessions,
            hasLocker
        );

        System.out.println(result);
        pause();
    }


    // Records a gym visit for the most recently registered member
    // in the currently active branch.
    private void recordGymVisit() {

        // Clear the console before displaying the menu
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("          RECORD GYM VISIT");
        System.out.println("========================================");

        // Check if an active branch exists
        if (activeBranch() == null) {

            // Display error message
            System.out.println("Error: No active branch selected.");

            // Pause before returning
            pause();
            return;
        }

        // Record the visit and display the result message
        System.out.println(
            activeBranch().recordVisitForLastMember()
        );

        // Pause before returning
        pause();
    }


    // Displays statistics for all branches and the last member in the active branch.
    private void viewMemberSummary() {

        clear();

        System.out.println("========================================");
        System.out.println("          MEMBER SUMMARY");
        System.out.println("========================================");

        if (branchCount == 0) {
            System.out.println("No branches have been created.");
            pause();
            return;
        }

        // Display statistics for every branch
        for (int i = 0; i < branchCount; i++) {
            System.out.println(branches[i].getBranchStatsSummary());
            System.out.println();
        }

        System.out.println("========================================");
        System.out.println("      ACTIVE BRANCH LAST MEMBER");
        System.out.println("========================================");

        if (activeBranch() == null) {
            System.out.println("No active branch selected.");
        }
        else {
            System.out.println(activeBranch().getLastMemberSummary());
        }

        pause();
    }


    // Compares the total cost of two different membership plans.
    private void compareMembershipPlans() {

        clear();

        System.out.println("========================================");
        System.out.println("       COMPARE MEMBERSHIP PLANS");
        System.out.println("========================================");

        // Get first plan
        String firstPlanName = getPlanType("Enter first plan name: ");
        double firstPlanPrice = getPlanPriceByName(firstPlanName);

        // Get second plan
        String secondPlanName = getPlanType("Enter second plan name: ");

        // Ensure the plans are different
        while (firstPlanName.equalsIgnoreCase(secondPlanName)) {
            System.out.println("Error: Plans must be different.");
            secondPlanName = getPlanType("Enter second plan name: ");
        }

        double secondPlanPrice = getPlanPriceByName(secondPlanName);

        // Get duration
        int duration = getIntInRange("Enter duration in months: ", 1, 12);

        // Calculate totals with GST
        double firstTotal = firstPlanPrice * duration * 1.10;
        double secondTotal = secondPlanPrice * duration * 1.10;

        System.out.println("----------------------------------------");
        System.out.println(firstPlanName + " Total: $" + formatCurrency(firstTotal));
        System.out.println(secondPlanName + " Total: $" + formatCurrency(secondTotal));
        System.out.println("----------------------------------------");

        if (firstTotal < secondTotal) {
            System.out.println(firstPlanName + " is cheaper by $" + formatCurrency(secondTotal - firstTotal));
        }
        else if (secondTotal < firstTotal) {
            System.out.println(secondPlanName + " is cheaper by $" + formatCurrency(firstTotal - secondTotal));
        }
        else {
            System.out.println("Both plans cost the same.");
        }

        pause();
    }


    // Registers a new member with a randomly selected promotional offer.
    private void simulatePromotionalOffer() {

        clear();

        System.out.println("========================================");
        System.out.println("      SIMULATE PROMOTIONAL OFFER");
        System.out.println("========================================");

        if (activeBranch() == null) {
            System.out.println("Error: No active branch selected.");
            pause();
            return;
        }

        int memberId = getIntInRange("Enter member ID: ", 1, 999999);
        String name = getNonEmptyString("Enter member name: ");

        String planName = getPlanType("Enter membership plan: ");
        double planPrice = getPlanPriceByName(planName);

        int duration = getIntInRange("Enter duration in months: ", 1, 12);

        boolean hasTrainer = getYesNo("Add personal trainer sessions? (Y/N): ");
        int trainerSessions = 0;

        if (hasTrainer) {
            trainerSessions = getIntInRange("Enter trainer sessions per month (1-4): ", 1, 4);
        }

        boolean hasLocker = getYesNo("Add locker rental? (Y/N): ");

        System.out.println(
            activeBranch().registerPromotionalMember(
                memberId,
                name,
                planName,
                planPrice,
                duration,
                hasTrainer,
                trainerSessions,
                hasLocker
            )
        );

        pause();
    }


    // Modifies the most recently registered member in the active branch.
    private void modifyLastRegistration() {

        clear();

        System.out.println("========================================");
        System.out.println("       MODIFY LAST REGISTRATION");
        System.out.println("========================================");

        if (activeBranch() == null) {
            System.out.println("Error: No active branch selected.");
            pause();
            return;
        }

        if (activeBranch().getMemberCount() <= 0) {
            System.out.println("Error: No members are registered in this branch.");
            pause();
            return;
        }

        String newPlanName = activeBranch().getLastMemberPlanName();
        int newDuration = activeBranch().getLastMemberDuration();
        boolean newHasTrainer = activeBranch().getLastMemberHasTrainer();
        int newTrainerSessions = activeBranch().getLastMemberTrainerSessions();
        boolean newHasLocker = activeBranch().getLastMemberHasLocker();

        System.out.println("Enter new plan name or press Enter to keep current (" + newPlanName + "): ");
        String planInput = scanner.nextLine().trim();

        if (!planInput.isEmpty()) {
            while (getValidatedPlanFromText(planInput) == null) {
                System.out.println("Error: " + planInput + " is Not a Valid Plan");
                System.out.println("Enter new plan name or press Enter to keep current: ");
                planInput = scanner.nextLine().trim();

                if (planInput.isEmpty()) {
                    break;
                }
            }

            if (!planInput.isEmpty()) {
                newPlanName = getValidatedPlanFromText(planInput);
            }
        }

        double newPlanPrice = getPlanPriceByName(newPlanName);

        System.out.println("Enter new duration or press Enter to keep current (" + newDuration + "): ");
        String durationInput = scanner.nextLine().trim();

        if (!durationInput.isEmpty()) {
            while (true) {
                try {
                    int value = Integer.parseInt(durationInput);

                    if (value >= 1 && value <= 12) {
                        newDuration = value;
                        break;
                    }
                    else {
                        System.out.println("Error: " + durationInput + " Not In Range 1-12");
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Error: " + durationInput + " Not A Number");
                }

                System.out.println("Enter new duration or press Enter to keep current: ");
                durationInput = scanner.nextLine().trim();

                if (durationInput.isEmpty()) {
                    break;
                }
            }
        }

        System.out.println("Change trainer option? Current (" + (newHasTrainer ? "Yes" : "No") + ") (Y/N or Enter to keep): ");
        String trainerInput = scanner.nextLine().trim();

        if (!trainerInput.isEmpty()) {
            while (!trainerInput.equalsIgnoreCase("y")
                    && !trainerInput.equalsIgnoreCase("yes")
                    && !trainerInput.equalsIgnoreCase("n")
                    && !trainerInput.equalsIgnoreCase("no")) {

                System.out.println("Error: " + trainerInput + " is Not a Yes or No");
                System.out.println("Change trainer option? (Y/N or Enter to keep): ");
                trainerInput = scanner.nextLine().trim();

                if (trainerInput.isEmpty()) {
                    break;
                }
            }

            if (trainerInput.equalsIgnoreCase("y") || trainerInput.equalsIgnoreCase("yes")) {
                newHasTrainer = true;
                newTrainerSessions = getIntInRange("Enter trainer sessions or press Enter to keep current (" + newTrainerSessions + "): ", 1, 4);
            }
            else if (trainerInput.equalsIgnoreCase("n") || trainerInput.equalsIgnoreCase("no")) {
                newHasTrainer = false;
                newTrainerSessions = 0;
            }
        }

        

        System.out.println("Change locker option? Current (" + (newHasLocker ? "Yes" : "No") + ") (Y/N or Enter to keep): ");
        String lockerInput = scanner.nextLine().trim();

        if (!lockerInput.isEmpty()) {
            while (!lockerInput.equalsIgnoreCase("y")
                    && !lockerInput.equalsIgnoreCase("yes")
                    && !lockerInput.equalsIgnoreCase("n")
                    && !lockerInput.equalsIgnoreCase("no")) {

                System.out.println("Error: " + lockerInput + " is Not a Yes or No");
                System.out.println("Change locker option? (Y/N or Enter to keep): ");
                lockerInput = scanner.nextLine().trim();

                if (lockerInput.isEmpty()) {
                    break;
                }
            }

            if (lockerInput.equalsIgnoreCase("y") || lockerInput.equalsIgnoreCase("yes")) {
                newHasLocker = true;
            }
            else if (lockerInput.equalsIgnoreCase("n") || lockerInput.equalsIgnoreCase("no")) {
                newHasLocker = false;
            }
        }

        System.out.println(
            activeBranch().modifyLastRegistration(
                newPlanName,
                newPlanPrice,
                newDuration,
                newHasTrainer,
                newTrainerSessions,
                newHasLocker
            )
        );

        pause();
    }


    // Cancels the most recently registered member
    // from the currently active branch.
    private void cancelLastRegistration() {

        // Clear the console before displaying the menu
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("       CANCEL LAST REGISTRATION");
        System.out.println("========================================");

        // Check if an active branch exists
        if (activeBranch() == null) {

            // Display error message
            System.out.println("Error: No active branch selected.");

            // Pause before returning
            pause();
            return;
        }

        // Cancel the registration and display the result message
        System.out.println(
            activeBranch().cancelLastRegistration()
        );

        // Pause before returning
        pause();
    }


    // Displays all registered members in the active branch.
    private void viewAllRegisteredMembers() {

        // Clear the console before displaying members
        clear();

        // Display menu heading
        System.out.println("========================================");
        System.out.println("       ALL REGISTERED MEMBERS");
        System.out.println("========================================");

        // Check if an active branch exists
        if (activeBranch() == null) {

            // Display error message
            System.out.println("Error: No active branch selected.");

            // Pause before returning
            pause();
            return;
        }

        // Display the formatted member table
        System.out.println(
            activeBranch().getAllMembersTable()
        );

        // Pause before returning
        pause();
    }


    // Saves the currently active branch data to a text file.
    private void saveBranchData() {
        clear();

        System.out.println("========================================");
        System.out.println("          SAVE BRANCH DATA");
        System.out.println("========================================");

        if (branchCount == 0) {
            System.out.println("Error: No branches available to save.");
            pause();
            return;
        }

        // Fixed file name used for saving active branch data.
        String filename = "branchData.txt";

        // Ask the active branch to save its own data.
        System.out.println(branches[activeBranchIndex].saveBranchData(filename));
        
        pause();
    }


    // Loads branch data from file and overwrites the current active branch.
    private void loadBranchData() {
        clear();

        System.out.println("========================================");
        System.out.println("          LOAD BRANCH DATA");
        System.out.println("========================================");

        // Fixed file name used for loading branch data.
        String filename = "branchData.txt";

        // Load a branch object from file through the GymBranch class.
        GymBranch loadedBranch = GymBranch.loadBranchData(
            filename,
            planNames,
            planPrices
        );

        if (loadedBranch == null) {
            System.out.println("Error: Could not load branch data.");
        }
        else {
            int matchingIndex = -1;

            for (int i = 0; i < branchCount; i++) {
                if (branches[i].getBranchId() == loadedBranch.getBranchId()) {
                    matchingIndex = i;
                }
            }

            if (matchingIndex != -1) {
                branches[matchingIndex] = loadedBranch;
                activeBranchIndex = matchingIndex;

                System.out.println("Existing branch found. Branch data overwritten.");
            }
            else {
                if (branchCount >= branches.length) {
                    System.out.println("Error: Cannot load branch. Maximum branches reached.");
                    pause();
                    return;
                }

                branches[branchCount] = loadedBranch;
                activeBranchIndex = branchCount;
                branchCount++;

                System.out.println("New branch loaded successfully.");
            }

            String errors = GymBranch.getLoadErrors();

            if (!errors.isEmpty()) {
                System.out.println("\nSome lines were skipped:");
                System.out.println(errors);
            }
        }

        pause();
    }

    // Returns the price for a membership plan name.
    private double getPlanPriceByName(String planName) {

        // Loop through all loaded plans
        for (int i = 0; i < planCount; i++) {

            // Check if the plan name matches
            if (planNames[i].equalsIgnoreCase(planName)) {

                // Return the matching plan price
                return planPrices[i];
            }
        }

        // Return zero if the plan was not found
        return 0.0;
    }


    


//
//
// Formatting
//
//

// Formats currency values to two decimal places.
private String formatCurrency(double value) {
    return String.format("%.2f", value);
}


//
//
// Safe input helpers
//
//


    // Safely gets a valid membership plan name from the user.
    private String getPlanType(String prompt) {

        // Continue prompting until a valid plan is entered
        while (true) {

            // Display available plans
            System.out.println("\nAvailable Plans: ");

            for (int i = 0; i < planCount; i++) {

                System.out.println(
                    "- " + planNames[i]
                    + " ($" + formatCurrency(planPrices[i]) + ")"
                );
            }

            // Get user input
            String input = getNonEmptyString(prompt);

            // Check if the entered plan exists
            for (int i = 0; i < planCount; i++) {

                if (planNames[i].equalsIgnoreCase(input)) {

                    // Return the correctly formatted stored plan name
                    return planNames[i];
                }
            }

            // Display error message if plan does not exist
            System.out.println(
                "Error: " + input + " is Not a Valid Plan"
            );

            pause();
        }
    }


    // Safely gets an integer input from the user within a specified range.
    // Continues prompting the user until a valid number is entered.
    private int getIntInRange(String prompt, int min, int max) {

        // Infinite loop continues until a valid value is returned
        while (true) {

            // Display the input prompt to the user
            System.out.println(prompt);

            // Read the users full input as a String
            String input = scanner.nextLine();

            try {

                // Attempt to convert the input into an integer
                int value = Integer.parseInt(input);

                // Check if the value is within the allowed range
                if (value >= min && value <= max) {

                    // Return the validated integer
                    return value;
                }
                else {

                    // Display range error message if number is outside limits
                    System.out.println("Error: " + input + " Not In Range " + min + "-" + max);
                    pause();
                }
            }

            // Handles invalid numeric input such as letters or symbols
            catch (NumberFormatException e) {

                // Display error message for invalid number format
                System.out.println("Error: " + input + " Not A Number");
                pause();
            }
        }
    }

    // Returns the correctly stored plan name if the entered text matches a valid plan.
    private String getValidatedPlanFromText(String input) {
        for (int i = 0; i < planCount; i++) {
            if (planNames[i].equalsIgnoreCase(input)) {
                return planNames[i];
            }
        }

        return null;
    }


    // Safely gets a non-empty String input from the user.
    // Continues prompting until valid text is entered.
    private String getNonEmptyString(String prompt) {

        // Infinite loop continues until a valid value is returned
        while (true) {

            // Display the input prompt to the user
            System.out.println(prompt);

            // Read the users full input as a String
            String input = scanner.nextLine();

            // Remove leading and trailing whitespace
            input = input.trim();

            // Check if the input is not blank
            if (!input.isEmpty()) {

                // Return the validated String
                return input;
            }
            else {

                // Display error message for blank input
                System.out.println("Error: Input Cannot Be Blank");
            }
        }
    }


    // Safely gets a Yes or No response from the user.
    // Continues prompting until a valid response is entered.
    private boolean getYesNo(String prompt) {
        // Infinite loop continues until a valid value is returned
        while (true) {

            // Display the input prompt to the user
            System.out.println(prompt);

            // Read the users full input as a String
            String input = scanner.nextLine();

            // Convert input to lowercase and validate response
            switch (input.toLowerCase()) {

                // Valid Yes responses
                case "yes":
                case "y":
                    return true;

                // Valid No responses
                case "no":
                case "n":
                    return false;

                // Handles invalid Yes/No input
                default:

                    // Display error message for invalid response
                    System.out.println("Error: " + input + " is Not a Yes or No");
                    pause();
            }
        }
    }


    // Pauses the program until the user presses Enter.
    // Used to give the user time to read output before continuing.
    private void pause() {

        // Display pause message to the user
        System.out.print("\nPress Enter to continue...");

        // Wait for the user to press Enter
        scanner.nextLine();
    }


    // Simulates clearing the console by printing multiple blank lines.
    // Used to improve console readability between menu screens.
    private void clear() {

        // Print 20 blank lines to push previous content off screen
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
