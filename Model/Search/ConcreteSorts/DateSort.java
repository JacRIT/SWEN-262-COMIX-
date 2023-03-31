package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class DateSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY release_year,release_month,release_day" ;
    }
    
}
