package Model.Search;

import Controllers.Utils.PreparedStatementContainer;

public abstract class SearchAlgorithm {
    
    private SortAlgorithm sort ;


    public void setSort(SortAlgorithm sort) {
        this.sort = sort;
    }

    public abstract PreparedStatementContainer search(int userId, String keyword) ;
}
