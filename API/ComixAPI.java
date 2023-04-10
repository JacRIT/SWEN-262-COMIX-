package Api;

import java.util.Map;

import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

interface ComixAPI {
    /**G
     * Sets the sort strategy of the current search strategy.
     * 
     * @param strategy the strategy being used bt the user.
     */
    void setSortStrategy(SortAlgorithm sortStrategy);

    /**G
     * Sets search strategy of Comic Controller allowing it to use the proper search
     * strategy
     * to search comics.
     * 
     * @param strategy the strategy being used by the user.
     */
    void setSearchStrategy(SearchAlgorithm searchStrategy);

    /**G
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

    /**G
     * Given a comic's id get the comic if it exists and return it from the database
     * 
     * @param comicId comic id to search for
     * @return Comic object that was found
     */
    Comic getComic(int comicId) throws Exception;

    /**G
     * 
     * @param userId Determines whether a personal collection is being searched
     *               within the database
     *               if userId == 1 then it well search all comics
     *               if userId > 1 then it will search the personal collection of
     *               the user with that id.
     * 
     * @return All the comics belonging to given personal collection from the given
     *         parameters sorted by publisher, series, volumes, and issue#
     *         Null if no personal collection available.
     */
    Comic[] browsePersonalCollection(int userId) throws Exception;

    /**G
     * Generates the statistics for a users collection.
     * 
     * Note :
     * Each time a comic is signed, its value is increased by 5%.
     * If a comics signature is verified by an authority then the comics value is
     * increased by an additional 20%
     * 
     * @param user
     * @return Map containing {"number of issues" : "#"
     *         "value" : "#"}
     * 
     *         null if user not logged in or user does not exist.
     */
    Map<String, Float> generateStatistics(User user) throws Exception;

    /**G
     * Creates a comic
     * 
     * @param userId Determines whether a comic is being created in a personal
     *               collection.
     *               if userId == 1 then it well search all comics
     *               if userId > 1 then it will search the personal collection of
     *               the user with that id.
     * @param comic  Comic being created.
     * @return Copy Id of the comic created.
     */
    Integer createComic(int userId, Comic comic) throws Exception;

    /**G
     * Signs a comic.
     * The user marks a comic as signed meaning that it has been
     * autographed by someone, e.g. a creator, a famous person, etc.
     * - A single comic may be signed more than once.
     * - Each time a comic is signed, its value is increased by 5%.
     * 
     * @param comic comic being signed
     * @param user  user signing comic
     * @return  Signature of the comic signed.
     *          Null : User Not logged in.
     */
    Signature signComic(Signature signature, Comic comic) throws Exception;

    /**G
     * unSigns a comic.
     * 
     * @param comic
     * @return true     : Comic is unSigned sucessfully
     *         false    : Comic is not successfully unsigned
     *                  - User Not logged in
     *                  - Comic does not exist
     *                  - Signature does not exist
     */
    Boolean unSignComic(Signature signature, Comic comic) throws Exception;

    /**G
     * Verifies a signed comic.
     * A signed comic may also be marked as "authenticated" meaning that the
     * signature has been verified by an authority. This increases the value of the
     * comic
     * by an additional 20%
     * 
     * @param signature the signature looking to be verified.
     * @param signedComic a previously signed comic to be verified.
     * 
     * @return  Signature that is now verified.
     *          Null 
     *              - User not logged in
     *              - Comic does not exist
     *              - Signature does not exist
     */
    Signature verifyComic(Signature signature, Comic signedComic) throws Exception;

    /**G
     * unVerifies a signed comic.
     * @param signedComic a previously signed and verified comic to be unverified.
     * @param signature signature to be unverified on signed comic.
     * @return true : comic sucessfully unverified
     *         false : comic NOT sucessfully unverified
     *                  - comic does not exist
     *                  - comic not previously verified and signed
     *                  - signature does not exist
     *                  - user not logged in.
     */
    Boolean unVerifyComic(Signature signature, Comic signedComic) throws Exception;

    /**
     * Marks a comic as "graded" on a scale of 1 to 10. The value of a graded comic
     * is adjusted according to the following formula:
     * - 1 = value * 0.10
     * - 2-10 = log10
     * (grade) * value
     * 
     * @param user  The user grading the comic.
     * @param comic The comic being graded
     * @param grade The grade on a scale of 1-10 of the comic.
     * @return true : comic sucessfully graded
     *         false : comic not sucessfully graded
     *         - comic does not exist
     *         - comic not in personal collection
     *         - grade is not inbetween 1-10
     */
    Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) throws Exception;

    /**
     * @param user  The user ungrading the comic.
     * @param comic The comic being ungraded
     * @return true : comic sucessfully ungraded
     *         false : comic not sucessfully ungraded
     *         - comic does not exist
     *         - comic not in personal collection
     *         - comic is not previously graded
     */
    Boolean ungradeComicInPersonalCollection(User user, Comic comic) throws Exception;

    /**
     * Marks a graded comic as "slabbed." This doubles the value of the comic
     * 
     * @param gradedComic graded comic being slabbed.
     * @param user        The user slabbing the comic.
     * @return true : Comic slabbed.
     *         false : Comic not sucessfully slabbed.
     *         - Comic already slabbed.
     *         - Comic is not graded.
     *         - Comic not in personal collection.
     *         - Comic does not exist.
     */
    Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception;

    /**
     * Unslabs a previously slabbed and graded comic in specified users collection.
     * 
     * @param gradedComic graded comic being unSlabbed.
     * @param user        The user unslabbing the comic.
     * @return true : Comic unslabbed.
     *         false : Comic not sucessfully unslabbed.
     *         - Comic not previously slabbed.
     *         - Comic is not graded.
     *         - Comic not in personal collection.
     *         - Comic does not exist.
     */
    Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception;

    /**
     * Adds a comic to a users personal collection.
     * 
     * @param comic The comic to be added to the personal collection.
     * @param user  The user adding the comic to their collection.
     * @return true : comic sucessfully added to personal collection
     *         false : comic not sucessfully added to personal collection
     *         - comic does not exist.
     *         - no personal collection found.
     */
    Boolean addComicToPersonalCollection(User user, Comic comic) throws Exception;

    /**
     * Removes a comic from personal colleciton.
     * 
     * @param comic The comic to be removed from the personal collection
     * @return true : comic sucessfully added to personal collection
     *         false : comic not sucessfully added to personal collection
     *         - comic does not exist.
     *         - comic does not exist in personal collection.
     *         - no personal collection found.
     */
    Boolean removeComicFromPersonalCollection(User user, Comic comic) throws Exception;

}