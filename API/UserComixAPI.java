package Api;



import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class UserComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy; 
    private ComicController comicController;

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
    public Comic[] searchComics(int userId, String keyword) {
        // return comicController.search(userId, keyword);
        throw new UnsupportedOperationException("Unimplemented method 'searchComics'");
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

    @Override
    public Comic[] browsePersonalCollectionHierarchy(int userId, String publish, String series, String volumes,
            String issue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'browsePersonalCollectionHierarchy'");
    }
}
