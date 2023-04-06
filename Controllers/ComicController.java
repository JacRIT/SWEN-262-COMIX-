package Controllers;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.JavaObjects.*;

import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Controllers.Utils.PreparedStatementContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public SearchAlgorithm getSearch() {
        return this.searchStrategy;
    }

    /**
     * Sets the search strategy with the given param
     * @param sortStrategy The strategy chosen for a search
     */
    public void setSort(SortAlgorithm sortStrategy){
        this.searchStrategy.setSort(sortStrategy);
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
     * Updates the comic (comic_info table) to reflect the given updated version of the comic.
     * The updated version of the comic should not have any id fields changed from the original.
     * Any information in fields specific to a copy will be ignored.
     * Only manually created comics can be edited.
     * @param updatedComic the new comic replacing the old data
     */
    public void updateComic(Comic updatedComic) {
        // check to make sure comic is not in the database-> if it is, throw error
        // update sql call
    }

    /**
     * Updates the copy (grade, slabbed, signatures) to reflect the given updated version of the copy.
     * The updated version of the comic should not have any id fields changed from the original.
     * Any information in fields not specific to a copy will be ignored.
     * @param userId the id of the user who owns the copy to be updated
     * @param updatedCopy a Comic instance containing the copy data to overwrite the current data
     * @return true if the user owns a copy with the given copy id and false if they did not and no changes were made
     */
    public boolean updateCopy(int userId, Comic updatedCopy) {
        // check to make sure the copy is owned by the given user by getting the collection id
        // copy id should 
        String sql = """
                SELECT COUNT(*) FROM comic_ownership 
                INNER JOIN collection_refrence ON collection_refrence.copy_fk = comic_ownership.id
                WHERE collection_refrence.collection_fk = ? AND comic_ownership.id = ?;
                """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(getCollectionIdFromUser(userId));
        obj.add(updatedCopy.getCopyId());
        ArrayList<Object> result = jdbcRead.executePreparedSQL(sql, obj);
        if ((long)result.get(0) == 0l)
            return false; 
        // update sql calls
        // grade and slabbed are in comic_ownership
        sql = """
                UPDATE comic_ownership
                SET grade = ?, slabbed = ?
                WHERE id = ?
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(updatedCopy.getGrade());
        psc.appendToObjects(updatedCopy.isSlabbed());
        psc.appendToObjects(updatedCopy.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
        // signatures split between signature_refrence and signature_info
        // check if signature is already in signature_reference
        for (Signature s : updatedCopy.getSignatures()) {
            sql = """
                SELECT authenticated FROM signature_info 
                INNER JOIN signature_refrence ON signature_refrence.signature_fk = signature_info.id
                WHERE signature_info.id = ? AND signature_refrence.copy_fk = ?;
            """;
            obj = new ArrayList<>();
            obj.add(s.getId());
            obj.add(updatedCopy.getCopyId());
            result = jdbcRead.executePreparedSQL(sql, obj);
            System.out.println("\tselect done");
            // if signature not already there, add it
            if (result.size() == 0) {
                // add entry to signature_info
                sql = """
                        INSERT INTO signature_info (s_name, authenticated)
                        VALUES (?, ?)
                        """;
                obj = new ArrayList<>();
                obj.add(s.getName());
                obj.add(s.isAuthenticated());
                int id = jdbcInsert.executePreparedSQLGetId(sql, obj);
                System.out.println("\tinfo insert done");
                // add entry to signature_refrence
                sql = """
                        INSERT INTO signature_refrence (signature_fk, copy_fk) 
                        VALUES (?, ?)
                        """;
                psc = new PreparedStatementContainer();
                psc.appendToSql(sql);
                psc.appendToObjects(id);
                psc.appendToObjects(updatedCopy.getCopyId());
                jdbcInsert.executePreparedSQL(psc);
                System.out.println("\trefrence insert done");
            }
            // if signature authentication has changed, update signature_info
            else if ((boolean)result.get(0) != s.isAuthenticated()) {
                sql = """
                        UPDATE signature_info SET authenticated = ? WHERE id = ?
                        """;
                
                psc = new PreparedStatementContainer();
                psc.appendToSql(sql);
                psc.appendToObjects(s.isAuthenticated());
                psc.appendToObjects(s.getId());
                jdbcInsert.executePreparedSQL(psc);
            }
        }
        return true;
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
     * UNFINISHED - signatures not accounted for
     * Gets the statistics, the total number of comics in the collection and the total value of the collection.
     * @param userId - the id of the user whose collection the statistics are being gathered for
     * @return a map with the keys of "count" and "value", with String values
     */
    public Map<String,String> getStatistics(int userId){
        /*
         * also need to know how many signatures and how many of them are authenticated
         */
        String sql = """
            SELECT comic_info.initial_value, comic_ownership.grade, comic_ownership.slabbed
            FROM comic_ownership
            INNER JOIN comic_info ON comic_info.id = comic_ownership.comic_fk
            INNER JOIN collection_refrence ON collection_refrence.copy_fk = comic_ownership.id
            WHERE collection_refrence.collection_fk = ?
            """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(getCollectionIdFromUser(userId));
        ArrayList<ArrayList<Object>> results = jdbcRead.readListofLists(sql, obj, 3);
        int count = results.size();
        double totalValue = 0;
        for (ArrayList<Object> entry : results) {
            // initial value
            double initialValue;
            if (entry.get(0) == null)
                initialValue = 0;
            else
                initialValue = (double) entry.get(0);
            // grade
            int grade;
            if (entry.get(1) == null)
                grade = 0;
            else
                grade = (int) entry.get(1);
            // slabbed
            boolean slabbed;
            if (entry.get(2) == null)
                slabbed = false;
            else
                slabbed = (boolean) entry.get(2);
            // caluculating total
            double value = initialValue;
            if (grade == 1)
                value = initialValue * 0.1;
            else if (grade > 1)
                value = initialValue * Math.log10(grade);
            if (slabbed)
                value *= 2;
            totalValue += value;
        }
        /*
        sql = """
                SELECT COUNT(*), authenticated FROM signature_info 
                INNER JOIN signature_refrence ON signature_refrence.signature_fk = signature_info.id
                INNER JOIN collection_refrence ON collection_refrence.copy_fk = signature_refrence.copy_fk
                WHERE collection_refrence.collection_fk = 3 
                GROUP BY authenticated;
            """;
        obj = new ArrayList<>();
        obj.add(getCollectionIdFromUser(userId));
        results = jdbcRead.readListofLists(sql, obj, 2);
        */
        // TBC when comics can actually be signed so that this can be tested


        Map<String,String> stats = new HashMap<>();
        stats.put("count", Integer.toString(count));
        stats.put("value", Double.toString(totalValue));
        return stats;
    }

    /**
     * Gets the collection id in the database for the given user.
     * @param userId - the id of the user to get the personal collection id of
     * @return - the id of the top-level personal collection owned by the given user
     */
    private int getCollectionIdFromUser(int userId) {
        String sql = "SELECT collection_fk FROM user_info WHERE id = ?";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(userId);
        ArrayList<Object> results = jdbcRead.executePreparedSQL(sql, obj);
        return (int) results.get(0);
    }


    public static void main(String[] args) throws Exception{
        ComicController cc = new ComicController();
        // Comic comic = cc.get(1);
        // System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        // SearchAlgorithm sa = new PartialKeywordSearch();
        // cc.setSearch(sa);
        // Comic[] comics = cc.search(2, "");
        // for (Comic comic : comics) {
        //     System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        // }
        // Map<String,String> stats = cc.getStatistics(2);
        // System.out.println("count = "+stats.get("count")+", total value = "+stats.get("value"));
        Comic comic = cc.get(14241);
        System.out.println(comic);
        // Signature s = new Signature(0, "Jim", true);
        // comic.addSignature(s);
        comic.getSignatures().get(0).setAuthenticated(true);
        cc.updateCopy(2, comic);
        System.out.println(cc.get(14241));

    }
}
