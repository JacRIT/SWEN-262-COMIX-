package Controllers.Utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import CSVComicReader;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Creator;
import Model.JavaObjects.Publisher;

/*
 * This class takes the Comic objects that are supplied from 
 * CSVComicReader and insertes them into the database. 
 * 
 * This class is kinda spaghetti, consult J if you want to reset the DB
 * 
 */

public class ComicImporter extends JDBCRead {
    
    private Comic   copy_target ;
    private int     collection_id ;

    // the following are only used for importComicNoChecks()
    // they are meant to keep track of all of the ids inside of the database to decrease the duplicate check queries
    private ArrayList<Comic> comics_in = new ArrayList<Comic>() ;
    private ArrayList<Publisher> pubs_in = new ArrayList<Publisher>() ;
    private ArrayList<Creator> auths_in = new ArrayList<Creator>() ;
    

    public ComicImporter() {
    }

    public void changeTarget(Comic new_target) {
        this.copy_target = new_target ;
    }
    public void changeCollection(int colelction) {
        this.collection_id = colelction ;
    }



    // public int checkComicExist() {
    //     /*
    //      * Checks if the details of 'copy_target' already exist inside of the 'comic_info' table
    //      * If it does, it will return the comic_id
    //      */
    // }

    // public Map<Creator,Integer> checkCreatorsExist() {
    //     /*
    //      * Checks if the Creator objects of 'copy_target' already exist inside of the 'creator_info' table
    //      * It will always return a map containing all of the instances of creators with their corresponding creator_id that DO exist.
    //      * If no creators exist in the database yet, it will return an empty map.
    //      * 
    //      * ALSO checks for correct creator refrences with the comic_id through the 'creator_renfrence' table
    //      */
    // }
    // private int checkSingleCreator(Creator target_creator) {
    //     /*
    //      * Checks if the details of 'target_creator' already exist inside of the 'creator_info' table
    //      * If it does, it will return the creator_id
    //      */
    // }

    // public Map<Publisher,Integer> checkPublishersExist() {
    //     /*
    //      * Checks if the Publisher objects of 'copy_target' already exist inside of the 'creator_info' table
    //      * It will always return a map containing all of the instances of publishers with their corresponding publisher_id that DO exist.
    //      * If no publishers exist in the database yet, it will return an empty map.
    //      * 
    //      * ALSO checks for correct publisher refrences with the comic_id through the 'publisher_renfrence' table
    //      */
    // }
    // private int checkSinglePublisher(Publisher target_publisher) {

    // }

    // // public Map<Character,Integer> checkCharactersExist() {
    // //     /*
    // //      * Checks if the Character objects of 'copy_target' already exist inside of the 'character_info' table
    // //      * It will always return a map containing all of the instances of characters with their corresponding character_id that DO exist.
    // //      * If no characters exist in the database yet, it will return an empty map.
    // //      *
    // //      * ALSO checks for correct character refrences with the comic_id through the 'character_renfrence' table
    // //      */
    // // }
    // // private int checkSingleCharacter(Publisher target_publisher) {
        
    // // }

    public void importComic() {
        int comic_id ;
        ArrayList<Integer> creator_ids = new ArrayList<Integer>() ;
        int pub_id ;
        /*
         * checks through comic_info, creator_info, publisher_info for duplicate information
         * creates a new row in comic_info with all of the new information
         */
        
        //-----------------------------------------------------------------------
        // check through comic_info for the stuff in copy_target
        //-----------------------------------------------------------------------
        String sql = """
                SELECT
                    id
                FROM
                    comic_info
                WHERE
                    series = ?
                    AND title = ?
                    AND issue_num = ?
                    AND descrip = ?
                    AND release_date = ?
                """;
        ArrayList<Object> prepareds = new ArrayList<Object>() ;
        
        prepareds.add(copy_target.getSeries()) ;
        prepareds.add(copy_target.getTitle()) ;
        prepareds.add(copy_target.getIssueNumber()) ;
        prepareds.add(copy_target.getDescription()) ;
        prepareds.add(copy_target.getPublicationDate()) ;

        ArrayList<Object> result = executePreparedSQL(sql, prepareds) ;
        if ( result.size() > 0 ) {
            return ; //comic_info is already in the database, no need to import anything
        } else {
            //-----------------------------------------------------------------------
            // if the stuff in copy_target is not in comic_info, create a new row
            //-----------------------------------------------------------------------
            sql = "INSERT INTO comic_info(series,title,issue_num,descrip,release_date, volume_num, initial_value, release_day, release_month, release_year) VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id" ;

            // compute release day month and year
            int r_day = 0 ;
            int r_month = 0 ;
            int r_year = 0 ;
            String date = copy_target.getPublicationDate() ;
            String[] split = date.split(" ") ;
            if (split.length == 1) {
                r_year = Integer.parseInt( split[0] ) ;
            } else if (split.length == 2) {
                     if (split[0].compareTo("Jan") ==0 ) {r_month = 1;}
                else if (split[0].compareTo("Feb") ==0 ) {r_month = 2;}
                else if (split[0].compareTo("Mar") ==0 ) {r_month = 3;}
                else if (split[0].compareTo("Apr") ==0 ) {r_month = 4;}
                else if (split[0].compareTo("May") ==0 ) {r_month = 5;}
                else if (split[0].compareTo("Jun") ==0 ) {r_month = 6;}
                else if (split[0].compareTo("Jul") ==0 ) {r_month = 7;}
                else if (split[0].compareTo("Aug") ==0 ) {r_month = 8;}
                else if (split[0].compareTo("Sep") ==0 ) {r_month = 9;}
                else if (split[0].compareTo("Oct") ==0 ) {r_month = 10;}
                else if (split[0].compareTo("Nov") ==0 ) {r_month = 11;}
                else if (split[0].compareTo("Dec") ==0 ) {r_month = 12;}
                r_year = Integer.parseInt( split[1] ) ;
            } else if (split.length == 3) {

                r_day = Integer.parseInt( split[1].substring(0, split[1].length()-1) ) ;
                     if (split[0].compareTo("Jan") ==0 ) {r_month = 1;}
                else if (split[0].compareTo("Feb") ==0 ) {r_month = 2;}
                else if (split[0].compareTo("Mar") ==0 ) {r_month = 3;}
                else if (split[0].compareTo("Apr") ==0 ) {r_month = 4;}
                else if (split[0].compareTo("May") ==0 ) {r_month = 5;}
                else if (split[0].compareTo("Jun") ==0 ) {r_month = 6;}
                else if (split[0].compareTo("Jul") ==0 ) {r_month = 7;}
                else if (split[0].compareTo("Aug") ==0 ) {r_month = 8;}
                else if (split[0].compareTo("Sep") ==0 ) {r_month = 9;}
                else if (split[0].compareTo("Oct") ==0 ) {r_month = 10;}
                else if (split[0].compareTo("Nov") ==0 ) {r_month = 11;}
                else if (split[0].compareTo("Dec") ==0 ) {r_month = 12;}
                r_year = Integer.parseInt( split[2] ) ;
            }

            prepareds.add(copy_target.getVolumeNumber());
            prepareds.add(copy_target.getInitialValue());
            prepareds.add(r_day);
            prepareds.add(r_month);
            prepareds.add(r_year);
            

            result.clear() ;
            result = executePreparedSQL(sql, prepareds) ;
            comic_id = (int) result.get(0) ;
        }

        //-----------------------------------------------------------------------
        // check through creator_info for the stuff in copy_target
        //-----------------------------------------------------------------------
        for (Creator creator : copy_target.getCreators()) {
            int creator_id ;
            sql = """
                    SELECT
                        id
                    FROM
                        creator_info
                    WHERE
                        c_name = ?
                """;
            prepareds.clear() ;
            prepareds.add(creator.getName()) ;

            result.clear();
            result = executePreparedSQL(sql, prepareds) ;
            if (result.size() > 0) {
                creator_ids.add( (int) result.get(0) ) ;
            } else {
                //-----------------------------------------------------------------------
                // if the stuff in copy_target is not in creator_info, create a new row
                //-----------------------------------------------------------------------
                sql = "INSERT INTO creator_info(c_name) VALUES (?) RETURNING id";

                result.clear();
                result = executePreparedSQL(sql, prepareds) ;
                creator_id = (int) result.get(0) ;
                creator_ids.add ( creator_id ) ;
                //-----------------------------------------------------------------------
                // also create a reference between the new creator and the new comic
                //-----------------------------------------------------------------------
                sql  = "INSERT INTO creator_refrence(creator_fk,comic_fk) VALUES (?,?)";
                prepareds.clear();
                prepareds.add(creator_id) ;
                prepareds.add(comic_id) ;
                executePreparedSQL(sql, prepareds) ;
            }
        }

        //-----------------------------------------------------------------------
        // check through publisher_info for the stuff in copy_target
        //-----------------------------------------------------------------------
        sql = """
                SELECT
                    id
                FROM
                    publisher_info
                WHERE
                    p_name = ?
            """;
        prepareds.clear() ;
        prepareds.add( copy_target.getPublisher().get(0).getName() ) ;

        result.clear();
        result = executePreparedSQL(sql, prepareds) ;

        if (result.size() > 0) {
            pub_id = (int) result.get(0) ;
        } else {
            //-----------------------------------------------------------------------
            // if the stuff in copy_target is not in creator_info, create a new row
            //-----------------------------------------------------------------------
            sql = "INSERT INTO publisher_info(p_name) VALUES (?) RETURNING id";

            result.clear();
            result = executePreparedSQL(sql, prepareds) ;
            pub_id = (int) result.get(0) ;
            //-----------------------------------------------------------------------
            // also create a reference between the new creator and the new comic
            //-----------------------------------------------------------------------
            sql  = "INSERT INTO publisher_refrence(publisher_fk,comic_fk) VALUES (?,?)";
            prepareds.clear();
            prepareds.add(pub_id) ;
            prepareds.add(comic_id) ;
            executePreparedSQL(sql, prepareds) ;
        }

    
    }

    // private void importComicNoChecks() {
    //     /*
    //      * This is an ADMIN ONLY COMMAND. If ran multiple times into the same database, duplicate entries are created.
    //      * This command only works under the pretenses that you import no duplicate comics, and the database starts empty
    //      */

    //     if (comics_in.contains(copy_target)) {return ;}// check for duplicate entries
    //     //-----------------------------------------------------------------------
    //     // import the comic
    //     //-----------------------------------------------------------------------
    //     JDBCInsert inserter = new JDBCInsert() ;
    //     String comic_sql = "INSERT INTO comic_info(series,title,volume_num,issue_num,initial_value,descrip,release_date) VALUES (?,?,?,?,?,?,?)";
    //     Object[] comic_prepared = {
    //         copy_target.getSeries(),
    //         copy_target.getTitle(),
    //         copy_target.getVolumeNumber(),
    //         copy_target.getIssueNumber(),
    //         copy_target.getInitial_value(),
    //         copy_target.getDescription(),
    //         copy_target.getPublicationDate()
    //     } ;

    //     inserter.executePreparedSQL(comic_sql, comic_prepared);
    //     comics_in.add(copy_target) ;

    //     //-----------------------------------------------------------------------
    //     // import the publishers and their refrences
    //     //-----------------------------------------------------------------------
    //     for (Publisher pub : copy_target.getPublisher() ) {
    //         int id = pubs_in.indexOf(pub) ;

    //         // if publisher is NOT in pubs_in
    //         if (id == -1) {
    //             // import publisher
    //             String pub_sql = "INSERT INTO publisher_info(p_name) VALUES (?)" ;
    //             Object[] pub_prepared = {pub.getName()} ;
    //             inserter.executePreparedSQL(pub_sql, pub_prepared) ;

    //             // add publisher to pubs_in
    //             pubs_in.add(pub) ;
    //             id = pubs_in.indexOf(pub) ;

    //         }

    //         // import publisher refrence
    //         String pubref_sql = "INSERT INTO publisher_refrence(publisher_fk, comic_fk) VALUES (?,?)" ;
    //         Object[] pubref_prepared = {id, comics_in.indexOf(copy_target)} ;
    //         inserter.executePreparedSQL(pubref_sql, pubref_prepared) ;

    //     }
    //     //-----------------------------------------------------------------------
    //     // import the creators and their refrences
    //     //-----------------------------------------------------------------------
    //     for (Creator aut : copy_target.getCreators() ) {
    //         int id = auths_in.indexOf(aut) ;

    //         // if creator is NOT in auths_in
    //         if (id == -1) {
    //             // import creator
    //             String auth_sql = "INSERT INTO creator_info(c_first_name, c_last_name) VALUES (?,?)" ;
    //             Object[] auth_prepared = {aut.getFirst_name(),aut.getLast_name()} ;
    //             inserter.executePreparedSQL(auth_sql, auth_prepared) ;

    //             // add publisher to pubs_in
    //             auths_in.add(aut) ;
    //             id = auths_in.indexOf(aut) ;

    //         }

    //         // import publisher refrence
    //         String authref_sql = "INSERT INTO creator_refrence(creator_fk, comic_fk) VALUES (?,?)" ;
    //         Object[] authref_prepared = {id, comics_in.indexOf(copy_target)} ;
    //         inserter.executePreparedSQL(authref_sql, authref_prepared) ;

    //     }

    //     //-----------------------------------------------------------------------
    //     // import the characters and their refrences
    //     //-----------------------------------------------------------------------

    //     //
    //     // There's nothing here...
    //     //



    //     //-----------------------------------------------------------------------
    //     // import the copy and its refrence to the database collection
    //     //-----------------------------------------------------------------------
        
    //     // import copy refrence
    //     String copy_sql = "INSERT INTO comic_ownership(comic_fk, comic_value, grade, slabbed) VALUES (?,?,?,?)" ;
    //     Object[] copy_prepared = {
    //         comics_in.indexOf(copy_target),
    //         copy_target.getValue(),
    //         copy_target.getGrade(),
    //         copy_target.isSlabbed()
    //     } ;
    //     inserter.executePreparedSQL(copy_sql, copy_prepared) ;
    //     //dont need to keep a seperate id list, copy_id will be the same value as comic_id

    //     //import copy_ownership
    //     String colref_sql = "INSERT INTO collection_refrence(collection_fk,copy_fk) VAlUES (?,?)" ;
    //     Object[] colref_prepared = {collection_id, comics_in.indexOf(copy_target)} ;
    //     inserter.executePreparedSQL(colref_sql, colref_prepared) ;

    // }

    private StringListContainer importComicNoChecks2() {
        /*
         * This is an ADMIN ONLY COMMAND. If ran multiple times into the same database, duplicate entries are created.
         * This command only works under the pretenses that you import no duplicate comics, and the database starts empty
         */

        if (comics_in.contains(copy_target)) {return null;}// check for duplicate entries

        String master_sql = "";

        ArrayList<Object> master_prepared = new ArrayList<Object>() ;
        //-----------------------------------------------------------------------
        // import the comic
        //-----------------------------------------------------------------------
        master_sql = master_sql.concat( "INSERT INTO comic_info(series,title,volume_num,issue_num,initial_value,descrip,release_date) VALUES (?,?,?,?,?,?,?); " );
        master_prepared.add(copy_target.getSeries());
        master_prepared.add(copy_target.getTitle());
        master_prepared.add(copy_target.getVolumeNumber());
        master_prepared.add(copy_target.getIssueNumber());
        master_prepared.add(copy_target.getInitialValue());
        master_prepared.add(copy_target.getDescription());
        master_prepared.add(copy_target.getPublicationDate());

        comics_in.add(copy_target) ;

        //-----------------------------------------------------------------------
        // import the publishers and their refrences
        //-----------------------------------------------------------------------
        for (Publisher pub : copy_target.getPublisher() ) {
            int id = pubs_in.indexOf(pub) ;

            // if publisher is NOT in pubs_in
            if (id == -1) {
                // import publisher
                master_sql = master_sql.concat("INSERT INTO publisher_info(p_name) VALUES (?); ");
                master_prepared.add( pub.getName() );

                // add publisher to pubs_in
                pubs_in.add(pub) ;
                id = pubs_in.indexOf(pub) ;

            }

            // import publisher refrence
            master_sql = master_sql.concat( "INSERT INTO publisher_refrence(publisher_fk, comic_fk) VALUES (?,?); " );
            master_prepared.add(id); 
            master_prepared.add(comics_in.indexOf(copy_target) );

        }
        //-----------------------------------------------------------------------
        // import the creators and their refrences
        //-----------------------------------------------------------------------
        for (Creator aut : copy_target.getCreators() ) {
            int id = auths_in.indexOf(aut) ;

            // if creator is NOT in auths_in
            if (id == -1) {
                // import creator
                master_sql = master_sql.concat( "INSERT INTO creator_info(c_name) VALUES (?); " );
                master_prepared.add(aut.getName() );

                // add publisher to pubs_in
                auths_in.add(aut) ;
                id = auths_in.indexOf(aut) ;

            }

            // import publisher refrence
            master_sql = master_sql.concat( "INSERT INTO creator_refrence(creator_fk, comic_fk) VALUES (?,?); " );
            master_prepared.add(id);
            master_prepared.add(comics_in.indexOf(copy_target) );

        }

        //-----------------------------------------------------------------------
        // import the characters and their refrences
        //-----------------------------------------------------------------------

        //
        // There's nothing here...
        //



        //-----------------------------------------------------------------------
        // import the copy and its refrence to the database collection
        //-----------------------------------------------------------------------
        
        // import copy refrence
        master_sql = master_sql.concat( "INSERT INTO comic_ownership(comic_fk, comic_value, grade, slabbed) VALUES (?,?,?,?); " );
        master_prepared.add(comics_in.indexOf(copy_target) );
        master_prepared.add(copy_target.getValue() );
        master_prepared.add(copy_target.getGrade() );
        master_prepared.add(copy_target.isSlabbed() );
        //dont need to keep a seperate id list, copy_id will be the same value as comic_id

        //import copy_ownership
        master_sql = master_sql.concat("INSERT INTO collection_refrence(collection_fk,copy_fk) VAlUES (?,?); " );
        master_prepared.add(collection_id);
        master_prepared.add(comics_in.indexOf(copy_target) );

        
        StringListContainer returns = new StringListContainer(master_sql, master_prepared) ;

        return returns ;

    }






    public static void main(String[] args) {
        /** 
        Scanner reader = new Scanner(System.in);
        System.out.println("Type \"I know what I'm doing, and I'm prepared for the consequences of my actions\" to proceed: ");
        String pass = reader.nextLine() ;
        reader.close();
        if (pass.compareTo("I know what I'm doing, and I'm prepared for the consequences of my actions") != 0) {
            System.out.println("Consult your team members first!");
            return ;
        }

        try {
            long start = System.currentTimeMillis() ;
            JDBCInsert createuser = new JDBCInsert() ;
            createuser.executeSQL("INSERT INTO user_info(first_name,last_name,username) VALUES ('D','B','Database'); INSERT INTO collection_info(nickname) VALUES ('Database Comics'); INSERT INTO collection_ownership(collection_fk, user_fk) VALUES (1,1);") ;
            System.out.println("Database collection setup completed...");
            //CHANGED CODE: OLD IMPLEMENTATION FOR BEFORE ADAPTER
            //CSVComicReader read = new CSVComicReader("./comics.csv") ;
            ComicImporter importer = new ComicImporter() ;
            importer.comics_in.add(null) ;
            importer.pubs_in.add(null) ;
            importer.auths_in.add(null) ;

            importer.changeCollection(1) ;
            

            Comic target = read.getNextComic() ;
            target = read.getNextComic() ;
            target = read.getNextComic() ;


            System.out.println("Creating Connection...");
            String URL = "jdbc:postgresql://jdb1.c4qx1ly4rhvr.us-east-2.rds.amazonaws.com:5432/postgres" ;
            String USER = "swen262" ;
            String PASS = "bubbles" ;
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);) {

            while (target != null) {
                importer.changeTarget(target) ;

                StringListContainer SQL_prepareds = importer.importComicNoChecks2() ;
                if (SQL_prepareds != null) {
                    String SQL = SQL_prepareds.string ;
                    ArrayList<Object> prepareds = SQL_prepareds.objects ;

                    PreparedStatement stmt = conn.prepareStatement(SQL) ;

                    int x = 1 ;
                    System.out.println("Preparing Statement...");
                    for (Object obj :prepareds) {
                        stmt.setObject(x, obj);
                        x++ ;
                    }
                    System.out.println("Executing Command...");

                    stmt.executeUpdate();
                    System.out.println("Command Executed!");
                }

                target = read.getNextComic() ;
            }
            long end = System.currentTimeMillis() ;
            System.out.println("Operation completed! Time (ms): " + (end-start));

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        }
        

            System.out.println("Operation completed!");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        */

    }
}
