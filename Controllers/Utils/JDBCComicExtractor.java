package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.JavaObjects.*;
import Model.JavaObjects.Character;

public class JDBCComicExtractor extends JDBC {
    /*
     * This class is meant to take sql input which ONLY SELECTS copy_fk FROM collection_refrence!
     * returns Comic[]
     */
    
     private Connection conn ;

    public JDBCComicExtractor() throws Exception {
        // when the object is created, the connection is started.
        this.conn = DriverManager.getConnection(URL, USER, PASS);
    }

    public void closeConnection() throws Exception {
        this.conn.close();
    }

    public Comic[] getComic(String sql) throws Exception {
        //find the copy_id's of all of the copies to return. this is what @param String sql is for
        List<Integer> copy_ids = new ArrayList<Integer>() ;
        List<Comic> comics = new ArrayList<Comic>() ;

        Statement stmt = this.conn.createStatement() ;
        ResultSet rs = stmt.executeQuery(sql) ;
        while (rs.next()) {
            copy_ids.add(
                rs.getInt("copy_fk")
            );
        }

        //retrieve all of the relevant information for each of the copies through the helper method
        for (Integer i : copy_ids) {
            comics.add(
                getComicFromCopyId(i)
            ) ;
        }

        return comics.toArray(new Comic[0]) ;
        
    }
    public Comic[] getComic(PreparedStatementContainer statementDetails) throws Exception {
        //find the copy_id's of all of the copies to return. this is what @param String sql is for
        List<Integer> copy_ids = new ArrayList<Integer>() ;
        List<Comic> comics = new ArrayList<Comic>() ;

        PreparedStatement stmt = this.conn.prepareStatement(statementDetails.getSql()) ;
        int i = 1;
        for (Object o : statementDetails.getObjects()) {
            stmt.setObject(i, o);
            i++ ;
        }
        ResultSet rs = stmt.executeQuery() ;
        while (rs.next()) {
            copy_ids.add(
                rs.getInt("copy_fk")
            );
        }

        //retrieve all of the relevant information for each of the copies through the helper method
        for (Integer j : copy_ids) {
            comics.add(
                getComicFromCopyId(j)
            ) ;
        }

        return comics.toArray(new Comic[0]) ;
    }

    public Comic getComicFromCopyId(int copy_id) throws Exception {
        
        int         comic_id ;
        String      series ;
        String      title ;
        int         volume_number ;
        String      issue_number ;
        float       initial_value ; 
        String      description ;
        String      release_date ;

        int value ;
        int grade ;
        boolean slabbed ;

        ArrayList<Publisher> publishers = new ArrayList<Publisher>();
        ArrayList<Creator> creators = new ArrayList<Creator>();
        ArrayList<Character> characters = new ArrayList<Character>();
        ArrayList<Signature> signatures = new ArrayList<Signature>();


        //get info from comic_info
        PreparedStatement stmt = this.conn.prepareStatement(
            "SELECT * FROM comic_info INNER JOIN comic_ownership ON comic_ownership.comic_fk = comic_info.id WHERE comic_ownership.id = ? LIMIT 1"
        );
        stmt.setInt(1, copy_id);
        ResultSet rs = stmt.executeQuery();

        if ( rs.next() ){
            
            comic_id =              rs.getInt("comic_fk") ;
            series =                rs.getString("series") ;
            title =                 rs.getString("title") ;
            volume_number =         rs.getInt("volume_num") ;
            issue_number =          rs.getString("issue_num") ;
            initial_value =         rs.getInt("initial_value") ;
            description =           rs.getString("descrip") ;
            release_date =          rs.getString("release_date") ;

            value =                 rs.getInt("comic_value") ;
            grade =                 rs.getInt("grade") ;
            slabbed =               rs.getBoolean("slabbed") ;

        } else {
            return null;
        }
        stmt.close();


        //get info from publisher_info
        PreparedStatement stmt2 = this.conn.prepareStatement(
            "SELECT * FROM publisher_info INNER JOIN publisher_refrence ON publisher_refrence.publisher_fk = publisher_info.id WHERE publisher_refrence.comic_fk = ?"
        );
        stmt2.setInt(1, comic_id);
        ResultSet rs2 = stmt2.executeQuery();
        while(rs2.next()){
            publishers.add(
                new Publisher(
                    rs2.getInt("id"),
                    rs2.getString("p_name")
                )
            );
        }
        stmt2.close();

        //get info from creator_info
        PreparedStatement stmt3 = this.conn.prepareStatement(
            "SELECT * FROM creator_info INNER JOIN creator_refrence ON creator_refrence.creator_fk = creator_info.id WHERE creator_refrence.comic_fk = ?"
        );
        stmt3.setInt(1, comic_id);
        ResultSet rs3 = stmt3.executeQuery();
        while(rs3.next()){
            creators.add(
                new Creator(
                    rs3.getInt("id"),
                    rs3.getString("c_name")
                )
            );
        }
        stmt3.close();

        //get info from character_info
        PreparedStatement stmt4 = this.conn.prepareStatement(
            "SELECT * FROM character_info INNER JOIN character_refrence ON character_refrence.character_fk = character_info.id WHERE character_refrence.comic_fk = ?"
        );
        stmt4.setInt(1, comic_id);
        ResultSet rs4 = stmt4.executeQuery();
        while(rs4.next()){
            characters.add(
                new Character(
                    rs4.getInt("id"),
                    rs4.getString("character_name")
                )
            );
        }
        stmt4.close();

        //get info from signature_info
        PreparedStatement stmt5 = this.conn.prepareStatement(
            "SELECT * FROM signature_info INNER JOIN signature_refrence ON signature_refrence.signature_fk = signature_info.id WHERE signature_refrence.copy_fk = ?"
        );
        stmt5.setInt(1, copy_id);
        ResultSet rs5 = stmt5.executeQuery();
        while(rs5.next()){
            signatures.add(
                new Signature(
                    rs5.getInt("id"),
                    rs5.getString("s_name"),
                    rs5.getBoolean("authenticated")
                )
            );
        }
        stmt5.close();
    
        //organize information into java object
        return new Comic(comic_id, copy_id, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, slabbed) ;
    }
    
    public static void main(String[] args) {
        try {
            JDBCComicExtractor comicExtractor = new JDBCComicExtractor() ;

            Comic[] comics = comicExtractor.getComic("SELECT copy_fk FROM collection_refrence INNER JOIN user_info ON user_info.collection_fk = collection_refrence.collection_fk WHERE user_info.id = 1 LIMIT 10") ; // gets the first 10 comics from the database users collection 
            for (Comic c: comics) {
                System.out.println(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
