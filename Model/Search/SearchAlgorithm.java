package Model.Search;

import Controllers.Utils.PreparedStatementContainer;
import Model.Search.ConcreteSorts.DefaultSort;

public abstract class SearchAlgorithm {
    
    protected SortAlgorithm sort ;

    public SearchAlgorithm() {
        this.sort = new DefaultSort() ;
    }


    public void setSort(SortAlgorithm sort) {
        this.sort = sort;
    }

    public abstract PreparedStatementContainer search(int userId, String keyword) ; 
    // goal is to get copy_ids from the database that coreespond to comics we're searching for. Getting from copy_id to comic.java object happens elsewhere
}
