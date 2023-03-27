package Model.Search;

public abstract class SearchAlgorithm {
    
    private SortAlgorithm sort ;

    SearchAlgorithm() {}

    public void setSort(SortAlgorithm sort) {
        this.sort = sort;
    }

    abstract String search(int userId, String keyword) ;
}
