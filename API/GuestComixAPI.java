package Api;

import java.util.Map;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;

public class GuestComixAPI implements ComixAPI {
    private ComicController comicController;

    public GuestComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.comicController.setSearch(new PartialKeywordSearch());
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.comicController.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(int userId, String keyword) throws Exception {
        return comicController.search(1, keyword);
    }

    @Override
    public Map<String, String> generateStatistics(User user) {
        return null;
    }

    @Override
    public String createComic(int userId, Comic comic) {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) {
        return null;
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
        return false;
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        return false;
    }

    @Override
    public Boolean addComicToPersonalCollection(User user, Comic comic) {
        return false;
    }

    @Override
    public Boolean removeComicFromPersonalCollection(User user, Comic comic) {
        return false;
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
        return false;
    }

    @Override
    public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        return false;
    }

    public Comic getComic(int comicId) throws Exception {
        return this.comicController.get(comicId);
    }
}