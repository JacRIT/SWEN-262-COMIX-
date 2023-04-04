package Api;

import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

interface ComixAPI {
    /**
     * Sets the sort strategy of the current search strategy.
     * 
     * @param strategy the strategy being used bt the user.
     */
    void setSortStrategy(SortAlgorithm sortStrategy);

    /**
     * Sets search strategy of Comic Controller allowing it to use the proper search
     * strategy
     * to search comics.
     * 
     * @param strategy the strategy being used by the user.
     */
    void setSearchStrategy(SearchAlgorithm searchStrategy);

    /**
     * Executes a search on all comics in database given a keyword.
     * 
     * @param keyword word being searched.
     * @return list of comics that match the search executed
     */
    Comic[] searchComics(String keyword) throws Exception;

    /**
     * 
     * @param userId  Determines whether a personal collection is being searched
     *                within the database
     *                if userId == 1 then it well search all comics
     *                if userId > 1 then it will search the personal collection of
     *                the user with that id.
     * @param publisher
     * @param series
     * @param volumes
     * @param issue
     * @return All the comics in a given personal collection from the given
     *         parameters sorted by publisher, series, volumes, and issue#
     *         Null if no personal collection available.
     */
    Comic[] browsePersonalCollection(int userId, String publisher, String series, String volume, String issue) throws Exception;

    /**
     * TODO : Figure out specific requirements on what statistics need to be
     * generated.
     * 
     * @param user
     * @return
     * 
     *          NULL if no personal collection available
     */
    float[] generateStatistics(User user);

    /**
     * Creates a comic
     * @param userId  Determines whether a comic is being created in a personal collection.
     *                if userId == 1 then it well search all comics
     *                if userId > 1 then it will search the personal collection of
     *                the user with that id.
     * @param comic Comic being created.
     * @return Name of the Comic Created.
     */
    String createComic(int userId, Comic comic);

}