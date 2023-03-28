package Api;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class GuestComixAPI implements ComixAPI{
    private SearchAlgorithm searchStrategy; 
    private ComicController comicController;
    private int noUser = -1;

    public GuestComixAPI() {
        searchStrategy = new ExactKeywordSearch();
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }
    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    @Override
    public Comic[] searchComics(String keyword) {
        return comicController.search(noUser, keyword);
    }

    @Override
    public HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> browsePersonalCollectionHierarchy() {
        return null;
    }

    @Override
    public float[] generateStatistics(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateStatistics'");
    }

    @Override
    public String createComic(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createComic'");
    }
}