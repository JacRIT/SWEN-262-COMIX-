package Api;

import java.util.Map;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSorts.BrowseSort;

public class UserComixAPI implements ComixAPI {
    private ComicController comicController;

    public UserComixAPI() throws Exception {
        this.comicController = new ComicController();
        comicController.setSearch(new PartialKeywordSearch());
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.comicController.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(int userId, String keyword) throws Exception {
        return comicController.search(userId, keyword);
    }

    @Override
    public Map<String, String> generateStatistics(User user) {
        return comicController.getStatistics(user.getId());
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

        SearchAlgorithm previousSearchAlgorithm = comicController.getSearch();

        comicController.setSearch(browseSearchAlgorithm);
        Comic[] personalCollection = comicController.search(userId, "");
        comicController.setSearch(previousSearchAlgorithm);

        return personalCollection;
    }

    @Override
    public Boolean signComic(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signComic'");
    }

    @Override
    public Boolean verifyComic(Comic signedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyComic'");
    }

    @Override
    public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gradeComicInPersonalCollection'");
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'slabGradedComicInPersonalCollection'");
    }

    @Override
    public Boolean addComicToPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addComicToPersonalCollection'");
    }

    @Override
    public Boolean removeComicFromPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeComicFromPersonalCollection'");
    }

    @Override
    public Boolean unSignComic(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unSignComic'");
    }

    @Override
    public Boolean unVerifyComic(Comic signedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unVerifyComic'");
    }

    @Override
    public Boolean ungradeComicInPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ungradeComicInPersonalCollection'");
    }

    @Override
    public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unslabGradedComicInPersonalCollection'");
    }
    
    public Comic getComic(int comicId) throws Exception {
        return this.comicController.get(comicId);
    }
}