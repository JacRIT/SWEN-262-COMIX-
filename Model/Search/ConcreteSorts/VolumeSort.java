package Model.Search.ConcreteSorts;

import Model.Search.SortAlgorithm;

public class VolumeSort implements SortAlgorithm {

    @Override
    public String sort() {
        return "ORDER BY volume_num";
    }
    
}
