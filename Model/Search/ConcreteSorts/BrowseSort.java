package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class BrowseSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY publisher_info.p_name, comic_info.series, comic_info.issue_num";
    }
    
}
