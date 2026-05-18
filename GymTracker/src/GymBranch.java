
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


    private int shortDurationCount;
    private int midDurationCount;
    private int longDurationCount;

    private int trainerCount;
    private int lockerCount;

    public GymBranch(int branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;

        members = new Member[MAX_MEMBERS];
        memberCount = 0;

        totalRevenue = 0.0;
        totalVisits = 0;


        shortDurationCount = 0;
        midDurationCount = 0;
        longDurationCount = 0;

        trainerCount = 0;
        lockerCount = 0;

    }
}


    // public String registerMember(int memberId, String name, String planType,
    //                             int durationMonths, boolean hasTrainer,
    //                             int trainerSessions, boolean hasLocker)

    // public String recordVisitForLastMember()

    // public String modifyLastRegistration(String newPlanType, int newDurationMonths)

    // public String cancelLastRegistration()

    // public String getLastMemberSummary()

    // public String getAllMembersTable()

    // public String getBranchStatsSummary()

    // public String saveBranchData(String filename)

    // public static GymBranch loadBranchData(String filename)

    // private boolean isDuplicateMemberId(int memberId)

    // private int generateUniqueMemberId()

    // private void addCounters(Member member)

    // private void removeCounters(Member member)

    // private void recalculateStatsFromMembers()

    // private boolean isValidMemberData(...)

    // private boolean isValidBranchData(...)


