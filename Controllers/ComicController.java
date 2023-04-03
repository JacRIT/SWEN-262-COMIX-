package Controllers;
import Model.Search.SearchAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.JavaObjects.*;

import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Controllers.Utils.PreparedStatementContainer;

import java.util.ArrayList;

import Controllers.Utils.JDBCComicExtractor;

public class ComicController {
    JDBCRead jdbcRead;
    JDBCInsert jdbcInsert;
    JDBCComicExtractor jdbcComicExtractor;

    public ComicController() throws Exception {
        this.jdbcRead = new JDBCRead();
        this.jdbcInsert = new JDBCInsert();
        this.jdbcComicExtractor = new JDBCComicExtractor();
    }

    private SearchAlgorithm searchStrategy;

    /**
     * Sets the search strategy with the given param
     * @param searchStrategy The strategy chosen for a search
     */
    public void setSearch(SearchAlgorithm searchStrategy){
        this.searchStrategy = searchStrategy;
    }

    /**
     * Searches the database for all comics matching the search term owned by the given user.
     * Based of the search set on the instance of ComicController, and the sort set on that search instance.
     * @param userId the user id to identify the collection
     * @param searchTerm the search term to look for
     * @return an array of Comics matching the search request, sorted
     */
    public Comic[] search(int userId, String searchTerm) throws Exception {
        PreparedStatementContainer psc = this.searchStrategy.search(userId, searchTerm);
        ArrayList<Object> copy_ids = this.jdbcRead.executePreparedSQL(psc.getSql(), psc.getObjects());
        int i = 0;
        Comic[] comics = new Comic[copy_ids.size()];
        for(Object o : copy_ids) {
            int copy_id = (int) o;
            Comic c = get(copy_id);
            comics[i++] = c;
        }
        return comics;
    }

    /**
     * Gets the comic from the the database
     * @param id the id of the COPY
     * @return the comic
     */
    public Comic get(int id) throws Exception{
        return jdbcComicExtractor.getComicFromCopyId(id);
    }

    /**
     * Updates the comic with the new comic
     * @param userId the userId to identify the collection
     * @param comic the comic to be changed
     * @param updatedComic the new comic replacing the old data
     */
    public void update(int userId, Comic comic, Comic updatedComic){

        //We'll be doing two update methods
        
    }

    /**
     * Deletes the copy and the collection reference, comic info is kept
     * @param userId the userId of the collection the comic is in
     * @param comic The comic to be deleted
     */
    public void delete(int userId, Comic comic){
        int copyId = comic.getCopyId();
        //deletes references and then the copy
        String deleteSql = "DELETE from signature_refrence, collection_refrence WHERE copy_fk = " + Integer.toString(copyId)
        + "; DELETE from comic_ownership WHERE id = " + Integer.toString(copyId);
        jdbcInsert.executeSQL(deleteSql);
    }

    /**
     * Creates a comic in a collection
     * @param userId the userId of the collection the comic will be in
     * @param comic The comic to be inserted
     */
    public void create(int userId, Comic comic){
        //adding it to comic_ownership
        String comicId = Integer.toString(comic.getId()) + ", ";
        String comicValue = Float.toString(comic.getValue()) + ", "; //converting here since database uses INTEGER
        String grade = Integer.toString(comic.getGrade()) + ", ";
        String slabbed = Boolean.toString(comic.isSlabbed());
        String sql = "INSERT INTO comic_ownership(comic_fk, comic_value, grade, slabbed)" +
        "VALUES(" + comicId + comicValue + grade + slabbed + ")";
        jdbcInsert.executeSQL(sql);
        addToCollection(userId, comic);

    }

    /**
     * Adds a comic copy to a collection
     * @param userId the userId of the collection the comic will be in
     * @param comic the comic to be added
     */
    public void addToCollection(int userId, Comic comic){
        String comicid = Integer.toString(comic.getCopyId());
        String sql = "INSERT INTO collection_refrence(collection_fk, copy_fk)" +
        "VALUES(" + Integer.toString(userId) + ", " + comicid + ")";
        jdbcInsert.executeSQL(sql);
    }

    /**
     * Removes a comic from a collection
     * @param userId the userId of the collection the comic will be removed from
     * @param comic the comic to be removed
     */
    public void removeFromCollection(int userId, Comic comic){
        String comicId = Integer.toString(comic.getId());
        String sql = "DELETE * FROM collection_refrence WHERE collection_fk = " + Integer.toString(userId)
        + "AND copy_fk = " + comicId;
        jdbcInsert.executeSQL(sql);
    }


    /**
     * Gets the statistics
     * @param userId
     * @return
     */
    public String getStatistics(int userId){
    //cpunts number of values and totals values of comics
        return null;
    }

    /**
     * 
     * @return
     */
    private Comic[] loadResults(){
        return null;
    }


    public static void main(String[] args) throws Exception{
        ComicController cc = new ComicController();
        // Comic comic = cc.get(1);
        // System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        SearchAlgorithm sa = new PartialKeywordSearch();
        cc.setSearch(sa);
        Comic[] comics = cc.search(1, "Control");
        for (Comic comic : comics) {
            System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        }

    }
}
