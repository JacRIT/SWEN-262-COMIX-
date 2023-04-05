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
     * @param userId  Determines whether a personal collection is being searched
     *                within the database
     *                if userId == 1 then it well search all comics
     *                if userId > 1 then it will search the personal collection of
     *                the user with that id.
     * @return list of comics that match the search executed
     */
    Comic[] searchComics(int userId, String keyword) throws Exception;

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


    /**
     * Signs a comic. 
     * The user marks a comic as signed meaning that it has been
        autographed by someone, e.g. a creator, a famous person, etc.
            - A single comic may be signed more than once.
            - Each time a comic is signed, its value is increased by 5%. 
     * @param comic
     * @return true : Comic is signed sucessfully 
     *         false : Comic is not successfully signed 
     *                  - comic does not exist in database.
     */
    Boolean signComic(Comic comic);

    /**
     * Verifies a signed comic.
     * A signed comic may also be marked as "authenticated" meaning that the
        signature has been verified by an authority. This increases the value of the comic
        by an additional 20%
     * @param signedComic a previously signed comic to be verified.
     * @return  true    : comic sucessfully signed
     *          false   : comic NOT sucessfully signed
     *                      - comic does not exist
     *                      - comic not previously signed
     */
    Boolean verifyComic(Comic signedComic);

    /**
     * Marks a comic as "graded" on a scale of 1 to 10. The value of a graded comic
        is adjusted according to the following formula:
            - 1 = value * 0.10
            - 2-10 = log10
            (grade) * value
     * @param user The user grading the comic.
     * @param comic The comic being graded
     * @param grade The grade on a scale of 1-10 of the comic.
     * @return  true    : comic sucessfully graded
     *          false   : comic not sucessfully graded
     *                      - comic does not exist
     *                      - comic not in personal collection
     *                      - grade is not inbetween 1-10
     */
    Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade);

    /**
     * Marks a graded comic as "slabbed." This doubles the value of the comic
     * @param user The user slabbing the comic.
     * @return  true    : Comic slabbed.
     *          false   : Comic not sucessfully slabbed.
     *                      - Comic already slabbed.
     *                      - Comic is not graded.
     *                      - Comic not in personal collection.
     *                      - Comic does not exist.
     */
    Boolean slabGradedComicInPersonalCollection(User user);

    /**
     * Adds a comic to a users personal collection.
     * @param comic The comic to be added to the personal collection.
     * @param user The user adding the comic to their collection.
     * @return  true    : comic sucessfully added to personal collection
     *          false   : comic not sucessfully added to personal collection
     *                      - comic does not exist.
     *                      - no personal collection found.
     */
    Boolean addComicToPersonalCollection(User user, Comic comic);

    /**
     * Removes a comic from personal colleciton.
     * @param comic The comic to be removed from the personal collection
     * @return  true    : comic sucessfully added to personal collection
     *          false   : comic not sucessfully added to personal collection
     *                      - comic does not exist.
     *                      - comic does not exist in personal collection.
     *                      - no personal collection found.
     */
    Boolean removeComicFromPersonalCollection(User user, Comic comic);


}