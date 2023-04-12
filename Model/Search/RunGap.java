package Model.Search;

public class RunGap {
    private int copyId ;
    private int issue ;

    public RunGap(int id, String issueNum) {
        this.copyId = id ;

        String newIssue = "" ;
        for (int i = 0; i < issueNum.length(); i++) {
            char check = issueNum.charAt(i) ;
            if (check > '0' && check < '9') {
                newIssue += check ;
            }
        }
        this.issue = Integer.parseInt(newIssue) ;
    }

    public int getCopyId() {
        return copyId;
    }

    public int getIssue() {
        return issue;
    }
    

    public boolean isConsecutive(RunGap other) {
        return this.getIssue() == (other.getIssue() + 1) ;
    }

    public boolean isDuplicate(RunGap other) {
        return this.getIssue() == other.getIssue() ;
    }

    /*
     * similar to isConsecutive, but will return the number of spaces between the other runGap and this RunGaps issue numbers
     * 
     * ex.
     *      this.issueNum = 1
     *      other.issueNum = 2
     * returns 0
     * 
     * ex. 2
     *      this.issueNum = 1
     *      other.issueNum = 4
     * returns 2
     */
    public int isConsecutiveWithGap(RunGap other) {
        return other.getIssue() - this.getIssue() - 1 ;
    }

}
