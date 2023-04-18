package Model.Search;

import java.util.ArrayList;

import Controllers.Utils.JDBCRead;
import Controllers.Utils.PreparedStatementContainer;
import Model.JavaObjects.Comic;
import Model.Search.ConcreteSorts.DefaultSort;

public abstract class SearchAlgorithm extends JDBCRead{
    
    protected SortAlgorithm sort ;

    public SearchAlgorithm() {
        this.sort = new DefaultSort() ;
    }


    public void setSort(SortAlgorithm sort) {
        this.sort = sort;
    }

    public abstract PreparedStatementContainer search(int userId, String keyword) ; 
    // goal is to get copy_ids from the database that coreespond to comics we're searching for. Getting from copy_id to comic.java object happens elsewhere

    public int[] executeSearch(int userId, String searchTerm) throws Exception {
        PreparedStatementContainer psc = search(userId, searchTerm);
        ArrayList<Object> copy_ids = executePreparedSQL(psc.getSql(), psc.getObjects());
        int i = 0;
        int[] comics = new int[copy_ids.size()];
        for (Object o : copy_ids) {
            int copy_id = (int) o;
            comics[i++] = copy_id;
        }
        return comics;
    }
}
