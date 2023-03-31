package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class IssueNumberSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY issue_num";
    }
    
}
