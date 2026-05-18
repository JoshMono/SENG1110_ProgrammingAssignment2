import java.io.File;
import java.util.Scanner;

public class GymSystemUI {
    private Scanner scanner;
    private GymBranch[] branches;
    private int branchCount;
    private int activeBranchIndex;

    
    private String[] planNames;
    private double[] planPrices;
    private String[] planFeatures;
    private int planCount;

    public static void main(String[] args) throws Exception {
        GymSystemUI ui = new GymSystemUI();
        ui.run();
    }

    public GymSystemUI() {
        scanner = new Scanner(System.in);

        branches = new GymBranch[3];

        branchCount = 0;
        activeBranchIndex = -1;
        
    }
    
    private void run() {
        scanner = new Scanner(System.in);
        System.out.println(System.getProperty("user.dir"));
        loadPlans();

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

    private void displayMainMenu() {
        System.out.println("========================================");
        System.out.println("        GYM MEMBERSHIP SYSTEM");
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

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                manageBranches();
                break;
            case 2:
                viewMembershipPlans();
                break;
            case 3:
                // registerNewMember();
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

    private void selectBranch() {
        clear();

        System.out.println("========================================");
        System.out.println("            SELECT BRANCH");
        System.out.println("========================================");

        if (branchCount == 0) {
            System.out.println("Error: No branches available.");
            pause();
            return;
        }

        int branchId = getIntInRange("Enter branch ID: ", 1, 999999);

        for (int i = 0; i < branchCount; i++) {
            if (branches[i].getBranchId() == branchId) {
                activeBranchIndex = i;
                System.out.println("Active branch changed to: " + branches[i].getBranchName());
                pause();
                return;
            }
        }

        System.out.println("Error: Branch ID not found.");
        pause();
    }

    private GymBranch activeBranch() {
        if (activeBranchIndex == -1) {
            return null;
        }

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

        // Handles file loading and parsing errors
        catch (Exception e) {

            // Display error message
            System.out.println("Error: Could Not Load plans.txt");

            // Load fallback default plans
            loadDefaultPlans();
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


    private void registerNewMember() {
        clear();
        System.out.println("========================================");
        System.out.println("        REGISTER NEW MEMBER");
        System.out.println("========================================");

        System.out.println("Register member UI will go here.");

        pause();
    }


    private void recordGymVisit() {
        clear();
        System.out.println("========================================");
        System.out.println("          RECORD GYM VISIT");
        System.out.println("========================================");

        System.out.println("Record visit UI will go here.");

        pause();
    }


    private void viewMemberSummary() {
        clear();
        System.out.println("========================================");
        System.out.println("          MEMBER SUMMARY");
        System.out.println("========================================");

        System.out.println("Member summary UI will go here.");

        pause();
    }


    private void compareMembershipPlans() {
        clear();
        System.out.println("========================================");
        System.out.println("       COMPARE MEMBERSHIP PLANS");
        System.out.println("========================================");

        System.out.println("Plan comparison UI will go here.");

        pause();
    }


    private void simulatePromotionalOffer() {
        clear();
        System.out.println("========================================");
        System.out.println("      SIMULATE PROMOTIONAL OFFER");
        System.out.println("========================================");

        System.out.println("Promotional offer UI will go here.");

        pause();
    }


    private void modifyLastRegistration() {
        clear();
        System.out.println("========================================");
        System.out.println("       MODIFY LAST REGISTRATION");
        System.out.println("========================================");

        System.out.println("Modify registration UI will go here.");

        pause();
    }


    private void cancelLastRegistration() {
        clear();
        System.out.println("========================================");
        System.out.println("       CANCEL LAST REGISTRATION");
        System.out.println("========================================");

        System.out.println("Cancel registration UI will go here.");

        pause();
    }


    private void viewAllRegisteredMembers() {
        clear();
        System.out.println("========================================");
        System.out.println("       ALL REGISTERED MEMBERS");
        System.out.println("========================================");

        System.out.println("Registered members UI will go here.");

        pause();
    }


    private void saveBranchData() {
        clear();
        System.out.println("========================================");
        System.out.println("          SAVE BRANCH DATA");
        System.out.println("========================================");

        System.out.println("Save branch data UI will go here.");

        pause();
    }


    private void loadBranchData() {
        clear();
        System.out.println("========================================");
        System.out.println("          LOAD BRANCH DATA");
        System.out.println("========================================");

        System.out.println("Load branch data UI will go here.");

        pause();
    }

//
//
// Formatting
//
//

private String formatCurrency(double value) {
    return String.format("%.2f", value);
}










//
//
// Safe input helpers
//
//

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


    // Safely gets a double input from the user within a specified range.
    // Continues prompting the user until a valid number is entered.
    private double getDoubleInRange(String prompt, double min, double max){
        // Infinite loop continues until a valid value is returned
        while (true) {

            // Display the input prompt to the user
            System.out.println(prompt);

            // Read the users full input as a String
            String input = scanner.nextLine();

            try {

                // Attempt to convert the input into a double
                double value = Double.parseDouble(input);

                // Check if the value is within the allowed range
                if (value >= min && value <= max) {

                    // Return the validated double
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


    // private String getPlanType(String prompt)


    // Pauses the program until the user presses Enter.
    // Used to give the user time to read output before continuing.
    private void pause() {

        // Display pause message to the user
        System.out.println("\nPress Enter to continue...");

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
