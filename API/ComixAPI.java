package Api;

import java.util.Map;

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
     * 
     * @return All the comics belonging to given personal collection from the given
     *         parameters sorted by publisher, series, volumes, and issue#
     *         Null if no personal collection available.
     */
    Comic[] browsePersonalCollection(int userId) throws Exception;

    /**
     * Generates the statistics for a users collection.
     * Note :
     *      Each time a comic is signed, its value is increased by 5%. 
     *      If a comics signature is verified by an authority then the comics value is increased by an additional 20%
     * @param user
     * @return Map containing {"number of issues" : "#" 
     *                          "value" : "#"}
     *         
     *          null if no personal collection available
     */
    Map<String, String> generateStatistics(User user);

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


    //R2 Requirements =================================================================

    /**
     * Signs a comic. 
     * The user marks a comic as signed meaning that it has been
        autographed by someone, e.g. a creator, a famous person, etc.
        ○ A single comic may be signed more than once.
        ○ Each time a comic is signed, its value is increased by 5%. 
     * @param comic
     * @return true : Comic is signed sucessfully 
     *         false : Comic is not successfully signed or does not exist in database.
     */
    Boolean signComic(Comic comic);

    /**
     * Verifies a signed comic.
     * A signed comic may also be marked as "authenticated" meaning that the
        signature has been verified by an authority. This increases the value of the comic
        by an additional 20%
     * @param comic
     * @return
     */
    Boolean verifyComic(Comic signedComic);

}