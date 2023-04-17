package Model.Search;

import java.util.ArrayList;

public class GapBin {

    private RunGap last ;
    private int runLength ;
    private int totalGap ;
    private ArrayList<RunGap> others ;

    private final int GAP_LIMIT ;

    public GapBin(RunGap first, int gapCap) {
        this.last = first ;
        this.runLength = 1 ;
        this.GAP_LIMIT = gapCap ;
        this.others = new ArrayList<RunGap>() ;
    }

    public int add(RunGap toAdd) {
        /*
         * 0 == add was successful
         * 1 == add was unsuccessful
         * 2 == add would add to the total gap, still successful
         */
        if (toAdd.isDuplicate(this.last)) {

            this.others.add(toAdd) ;

        } else if (toAdd.isConsecutive(this.last)) {

            this.others.add(this.last) ;
            this.last = toAdd ;
            this.runLength++ ;
            
        } else if ( (this.last.isConsecutiveWithGap(toAdd)+ totalGap) <= GAP_LIMIT ){

            this.totalGap += this.last.isConsecutiveWithGap(toAdd) ;

            this.others.add(this.last) ;
            this.last = toAdd ;
            this.runLength++ ;
            return 2 ;

        } else {
            return 1 ;
        }
        return 0 ;
    }

    public RunGap getLast() {
        return last;
    }

    public int getRunLength() {
        return runLength;
    }

    public ArrayList<RunGap> getOthers() {
        return others;
    }

    public int getTotalGap() {
        return totalGap;
    }

}
