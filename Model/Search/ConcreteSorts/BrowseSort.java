package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class BrowseSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY p_name, series, issue_num";
    }
    
}
