package Model.Search;

import java.util.ArrayList;

public class RunBin {
    private RunGap last ;
    private int runLength ;
    private ArrayList<RunGap> others ;

    public RunBin(RunGap first) {
        this.last = first ;
        this.runLength = 1 ;
        this.others = new ArrayList<RunGap>() ;
    }

    public boolean add(RunGap toAdd) {
        if (toAdd.isConsecutive(this.last)) {
            this.others.add(this.last) ;
            this.last = toAdd ;
            this.runLength++ ;
        } else if (toAdd.isDuplicate(this.last)) {
            this.others.add(toAdd) ;
        } else {
            return false ;
        }
        return true ;
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

}
