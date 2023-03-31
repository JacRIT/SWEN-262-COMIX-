package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class DefaultSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY series, title, issue_num" ;
    }
    
}
