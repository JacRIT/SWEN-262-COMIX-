package Api;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class GuestComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy;
    private ComicController comicController;

    public GuestComixAPI() {
        comicController = new ComicController();
        searchStrategy = new ExactKeywordSearch();
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(String keyword) {
        return comicController.search(1, keyword);
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
    public Comic[] browsePersonalCollection(int userId, String publish, String series, String volumes,
            String issue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'browsePersonalCollectionHierarchy'");
    }
}