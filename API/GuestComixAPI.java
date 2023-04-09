package Api;

import java.util.Map;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
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
    public String createComic(int userId, Comic comic) throws Exception {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) {
        return null;
    }

    @Override
    public Boolean signComic(Signature signature, Comic comic) {
        // Guests cannot sign a comic.
        return false;
    }

    @Override
    public Boolean unSignComic(Signature signature, Comic comic) {
        // Guests cannot unsign a comic.
        return false;
    }

    @Override
    public Boolean verifyComic(Signature signature, Comic signedComic) {
        // Guests cannot verify a Comic
        return false;
    }

    @Override
    public Boolean unVerifyComic(Signature signature, Comic signedComic) {
        // Guests cannot unVerify a comic.
        return false;
    }

    @Override
    public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gradeComicInPersonalCollection'");
    }

    @Override
    public Boolean ungradeComicInPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ungradeComicInPersonalCollection'");
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'slabGradedComicInPersonalCollection'");
    }

    @Override
    public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unslabGradedComicInPersonalCollection'");
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

    public Comic getComic(int comicId) throws Exception {
        return this.comicController.get(comicId);
    }
}