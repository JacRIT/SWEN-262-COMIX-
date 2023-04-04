package Api;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSorts.BrowseSort;

public class UserComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy;
    private ComicController comicController;

    public UserComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.searchStrategy = new ExactKeywordSearch();
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(String keyword) throws Exception {
        return comicController.search(1, keyword);
    }

    @Override
    public float[] generateStatistics(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateStatistics'");
    }

    @Override
    public String createComic(int userId, Comic comic) {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) throws Exception {
        SearchAlgorithm browseSearchAlgorithm = new PartialKeywordSearch();
        browseSearchAlgorithm.setSort(new BrowseSort());
        SearchAlgorithm previousSearchAlgorithm = this.searchStrategy;

        comicController.setSearch(browseSearchAlgorithm);
        Comic[] personalCollection = comicController.search(userId, "");
        comicController.setSearch(previousSearchAlgorithm);
        
        return personalCollection;
    }
}