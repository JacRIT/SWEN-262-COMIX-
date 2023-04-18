package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class TitleSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY title";
    }
    
}
