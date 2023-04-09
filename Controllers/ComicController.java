package Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Controllers.Utils.JDBCComicExtractor;
import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Controllers.Utils.PreparedStatementContainer;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

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
     * 
     * @param searchStrategy The strategy chosen for a search
     */
    public void setSearch(SearchAlgorithm searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchAlgorithm getSearch() {
        return this.searchStrategy;
    }

    /**
     * Sets the search strategy with the given param
     * 
     * @param sortStrategy The strategy chosen for a search
     */
    public void setSort(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    /**
     * Searches the database for all comics matching the search term owned by the
     * given user.
     * Based of the search set on the instance of ComicController, and the sort set
     * on that search instance.
     * 
     * @param userId     the user id to identify the collection
     * @param searchTerm the search term to look for
     * @return an array of Comics matching the search request, sorted
     */
    public Comic[] search(int userId, String searchTerm) throws Exception {
        PreparedStatementContainer psc = this.searchStrategy.search(userId, searchTerm);
        ArrayList<Object> copy_ids = this.jdbcRead.executePreparedSQL(psc.getSql(), psc.getObjects());
        int i = 0;
        Comic[] comics = new Comic[copy_ids.size()];
        for (Object o : copy_ids) {
            int copy_id = (int) o;
            Comic c = get(copy_id);
            comics[i++] = c;
        }
        return comics;
    }

    /**
     * Gets the comic from the the database
     * 
     * @param id the id of the COPY
     * @return the comic
     */
    public Comic get(int id) throws Exception {
        return jdbcComicExtractor.getComicFromCopyId(id);
    }

    /**
     * Updates the comic (comic_info table) to reflect the given updated version of
     * the comic.
     * The updated version of the comic should not have any id fields changed from
     * the original.
     * Any information in fields specific to a copy will be ignored.
     * Only manually created comics can be edited.
     * 
     * @param updatedComic the new comic replacing the old data
     */
    public void updateComic(Comic updatedComic) {
        // check to make sure comic is not in the database-> if it is, throw error
        // update sql call
    }

    /**
     * Updates the copy (grade, slabbed) to reflect the given updated
     * version of the copy.
     * This method does not update signatures.
     * The updated version of the comic should not have any id fields changed from
     * the original.
     * Any information in fields not specific to a copy will be ignored.
     * 
     * @param userId      the id of the user who owns the copy to be updated
     * @param updatedCopy a Comic instance containing the copy data to overwrite the
     *                    current data
     * @return true if the user owns a copy with the given copy id and false if they
     *         did not and no changes were made
     * @throws Exception
     */
    public boolean updateCopy(int userId, Comic updatedCopy) throws Exception {
        // check to make sure the copy is owned by the given user by getting the
        // collection id
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
        if ((long) result.get(0) == 0l)
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
        return true;
    }

    /**
     * Adds a signature to a copy of a comic.
     * @param copyId the id of the copy the signature is being added to
     * @param s the Signature being added to the copy
     * @return the Signature that was added with the updated id
     * @throws Exception
     */
    public Signature addSignature(int copyId, Signature s) throws Exception {
        // signatures split between signature_refrence and signature_info
        // add entry to signature_info
        String sql = """
                INSERT INTO signature_info (s_name, authenticated)
                VALUES (?, ?)
                """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(s.getName());
        obj.add(s.isAuthenticated());
        int id = jdbcInsert.executePreparedSQLGetId(sql, obj);
        System.out.println("\tinfo insert done");
        // add entry to signature_refrence
        sql = """
                INSERT INTO signature_refrence (signature_fk, copy_fk)
                VALUES (?, ?)
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(id);
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
        System.out.println("\trefrence insert done");
        s.setId(id);
        return s;
    }

    /**
     * Removes the given signature from the database and its copy.
     * @param s the Signature being removed (id must be correct)
     * @throws Exception
     */
    public void removeSignature(Signature s) throws Exception {
        // remove from signature_refrence
        String sql = "DELETE FROM signature_refrence WHERE signature_fk = ?";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(s.getId());
        jdbcInsert.executePreparedSQL(psc);
        // remove fro signature_info
        sql = "DELETE FROM signature_info WHERE id = ?";
        psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(s.getId());
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * Deletes the copy and the collection reference, comic info is kept
     * 
     * @param userId the userId of the collection the comic is in
     * @param comic  The comic to be deleted
     * @throws Exception
     */
    public void delete(int userId, Comic comic) throws Exception {
        int copyId = comic.getCopyId();
        // deletes references and then the copy
        String deleteRefSql = "DELETE from signature_refrence, collection_refrence WHERE copy_fk = ?;";
        String deleteCopySql = "DELETE from comic_ownership WHERE id = ?";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(deleteRefSql);
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
        // now deletes copy
        psc = new PreparedStatementContainer();
        psc.appendToSql(deleteCopySql);
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * UNFINISHED
     * Creates a comic and adds it to the user's personal collection.
     * 
     * @param userId the userId of the collection the comic will be in
     * @param comic  The comic to be inserted
     */
    public void create(int userId, Comic comic) throws Exception {
        // adding it to comic_info (comic)
        String sql = """
            INSERT INTO comic_info(series, title, vol_num, issue_num, initial_value, descrip, release_date)
            VALUES (?,?,?,?,?,?,?)
            """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(comic.getSeries());
        obj.add(comic.getTitle());
        obj.add(comic.getVolumeNumber());
        obj.add(comic.getIssueNumber());
        obj.add(comic.getInitialValue());
        obj.add(comic.getDescription());
        obj.add(comic.getPublicationDate());
        int comicId = jdbcInsert.executePreparedSQLGetId(sql, obj);
        comic.setId(comicId);

        // still need to check if publisher, creator, and characters exist
        // add if they do not exist (select from ---_info using id)
            // add to ---_info
        // and add to ---_refrence tables
        
        // creates a new copy (comic_ownership) and adds it to collection_refrence
        addToCollection(userId, comic);
    }

    /**
     * Adds a comic copy to a collection.
     * 
     * @param userId the userId of the collection the comic will be in
     * @param comic  the comic to be added (assumed that the copy has not been created)
     */
    public void addToCollection(int userId, Comic comic) throws Exception {
        // create a new copy of a comic (comic_ownership)
        String sql = "INSERT INTO comic_ownership(comic_fk, comic_value, grade, slabbed) VALUES(?, ?, ?, ?)";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(comic.getId());
        obj.add(comic.getValue());
        obj.add(comic.getGrade());
        obj.add(comic.isSlabbed());
        int copyId = jdbcInsert.executePreparedSQLGetId(sql, obj);
        // add new entry to collection_refrence
        sql = "INSERT INTO collection_refrence (collection_fk, copy_fk) VALUES (?,?)";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(getCollectionIdFromUser(userId));
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * Removes a comic from a collection
     * 
     * @param userId the userId of the collection the comic will be removed from
     * @param comic  the comic to be removed (copy id must be set)
     * @throws Exception
     */
    public void removeFromCollection(int userId, Comic comic) throws Exception {
        String sql = "DELETE * FROM collection_refrence WHERE collection_fk = ? AND copy_fk = ?";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(getCollectionIdFromUser(userId));
        psc.appendToObjects(comic.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * Gets the statistics: the total number of comics in the collection and the
     * total value of the collection.
     * 
     * @param userId - the id of the user whose collection the statistics are being
     *               gathered for
     * @return a map with the keys of "count" and "value", with String values
     * @throws Exception
     */
    public Map<String, String> getStatistics(int userId) throws Exception {
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
        // signatures
        sql = """
                    SELECT COUNT(*), authenticated FROM signature_info
                    INNER JOIN signature_refrence ON signature_refrence.signature_fk = signature_info.id
                    INNER JOIN collection_refrence ON collection_refrence.copy_fk = signature_refrence.copy_fk
                    WHERE collection_refrence.collection_fk = ?
                    GROUP BY authenticated;
                """;
        obj = new ArrayList<>();
        obj.add(getCollectionIdFromUser(userId));
        results = jdbcRead.readListofLists(sql, obj, 2);
        long totalNumSigs = (long) results.get(0).get(0) + (long) results.get(1).get(0); // first get row, then get
                                                                                         // count (index 0)
        // value increases by 5% for every signature
        for (int i = 0; i < totalNumSigs + .25; i++) {
            totalValue *= 1.05;
        }
        System.out.println(results.get(1).get(1));
        // value increased by an additional 20% for every authentication
        for (int i = 0; i < (long) results.get(1).get(0) + .25; i++) {
            totalValue *= 1.2;
        }
        // output
        Map<String, String> stats = new HashMap<>();
        stats.put("count", Integer.toString(count));
        stats.put("value", Double.toString(totalValue));
        return stats;
    }

    /**
     * Gets the collection id in the database for the given user.
     * 
     * @param userId - the id of the user to get the personal collection id of
     * @return - the id of the top-level personal collection owned by the given user
     * @throws Exception
     */
    private int getCollectionIdFromUser(int userId) throws Exception {
        String sql = "SELECT collection_fk FROM user_info WHERE id = ?";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(userId);
        ArrayList<Object> results = jdbcRead.executePreparedSQL(sql, obj);
        return (int) results.get(0);
    }

    public static void main(String[] args) throws Exception {
        ComicController cc = new ComicController();
        // Comic comic = cc.get(1);
        // System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        // SearchAlgorithm sa = new PartialKeywordSearch();
        // cc.setSearch(sa);
        // Comic[] comics = cc.search(2, "");
        // for (Comic comic : comics) {
        // System.out.println(comic.getTitle()+" "+comic.getId()+" "+comic.getCopyId());
        // }

        // Comic comic = cc.get(14241);
        // System.out.println(comic);
        // Signature s = new Signature(0, "Jim", true);
        // comic.addSignature(s);
        // comic.getSignatures().get(1).setAuthenticated(false);
        // cc.updateCopy(2, comic);
        // System.out.println(cc.get(14241));

        Map<String, String> stats = cc.getStatistics(2);
        System.out.println("count = " + stats.get("count") + ", total value = " + stats.get("value"));

    }
}
