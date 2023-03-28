package Api;

import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class UserComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy; 

    public UserComixAPI() {
        this.searchStrategy = new ExactKeywordSearch();
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
    public Comic[] executeSearch(int userId, Comic[] keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeSearch'");
    }
    @Override
    public Comic[] browse(String publisher, String series, String volume, String issue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'browse'");
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
