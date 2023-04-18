package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public Comic[] getComicFromCopyIArray(int[] copy_ids) throws Exception {
        
            if ( copy_ids.length < 1) {return null ;}

            String copyIdPreparedString = "(";
            for (int i = 0; i < copy_ids.length-1; i++) {
                copyIdPreparedString += String.valueOf( copy_ids[i] ) ;
                copyIdPreparedString += "," ;
            }
            copyIdPreparedString += String.valueOf( copy_ids[copy_ids.length-1] ) ;
            copyIdPreparedString += ")" ;
     
            Map<Integer,Comic> comics = new HashMap<Integer,Comic>();
            Comic[] resultComics = new Comic[copy_ids.length] ;


            int         copy_id ;
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

            //get info from comic_info
            String sql = """
                SELECT 
                    comic_info.id,
                    comic_info.series,
                    comic_info.title,
                    comic_info.volume_num,
                    comic_info.issue_num,
                    comic_info.initial_value,
                    comic_info.descrip,
                    comic_info.release_date,
                    comic_ownership.comic_fk,
                    comic_ownership.comic_value,
                    comic_ownership.grade,
                    comic_ownership.slabbed
                FROM 
                    comic_info 
                INNER JOIN 
                    comic_ownership ON comic_ownership.comic_fk = comic_info.id 
                WHERE 
                    comic_ownership.id IN 
            """;
            sql += copyIdPreparedString ;
            Statement stmt = this.conn.createStatement() ;
            ResultSet rs = stmt.executeQuery(sql);

            while ( rs.next() ){
                
                copy_id =               rs.getInt("id") ;
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
                comics.put(copy_id, new Comic(comic_id, copy_id, new ArrayList<Publisher>(), series, title, volume_number, issue_number, release_date, new ArrayList<Creator>(), new ArrayList<Character>(), description, initial_value, new ArrayList<Signature>(), value, grade, slabbed)) ;
            }
            stmt.close();


        //get info from publisher_info
        String sql2 = """
            SELECT 
                publisher_info.id,
                publisher_info.p_name,
                publisher_refrence.comic_fk
            FROM 
                publisher_info 
            INNER JOIN 
                publisher_refrence ON publisher_refrence.publisher_fk = publisher_info.id 
            WHERE 
                publisher_refrence.comic_fk IN 
            """;
        Statement stmt2 = this.conn.createStatement() ;
        sql2 += copyIdPreparedString ;
        ResultSet rs2 = stmt2.executeQuery(sql2);
        while(rs2.next()){
            comics.get(rs2.getInt("comic_fk")).addPublisher(
                new Publisher(
                    rs2.getInt("id"),
                    rs2.getString("p_name")
                )
            );
        }
        stmt2.close();

        

        //get info from creator_info
        String sql3 = """
            SELECT 
                creator_info.id,
                creator_info.c_name,
                creator_refrence.comic_fk 
            FROM 
                creator_info
            INNER JOIN 
                creator_refrence ON creator_refrence.creator_fk = creator_info.id 
            WHERE creator_refrence.comic_fk IN 
            """;
        Statement stmt3 = this.conn.createStatement() ;
        sql3 += copyIdPreparedString ;
        ResultSet rs3 = stmt3.executeQuery(sql3);
        while(rs3.next()){
            comics.get(rs3.getInt("comic_fk")).addCreator(
                new Creator(
                    rs3.getInt("id"),
                    rs3.getString("c_name")
                )
            );
        }
        stmt3.close();

        //get info from signature_info
        String sql5 = """
            SELECT 
                signature_info.id,
                signature_info.s_name,
                signature_info.authenticated,
                signature_refrence.copy_fk
            FROM 
                signature_info 
            INNER JOIN 
                signature_refrence ON signature_refrence.signature_fk = signature_info.id 
            WHERE signature_refrence.copy_fk IN 
            """;
        Statement stmt5 = this.conn.createStatement() ;
        sql5 += copyIdPreparedString ;
        ResultSet rs5 = stmt5.executeQuery(sql5);
        while(rs5.next()){
            comics.get(rs5.getInt("copy_fk")).addSignature(
                new Signature(
                    rs5.getInt("id"),
                    rs5.getString("s_name"),
                    rs5.getBoolean("authenticated")
                )
            );
        }
        stmt5.close();
    
        //organize information into java object
        for (int i = 0; i < copy_ids.length; i++) {
            resultComics[i] = comics.get(copy_ids[i]) ;
        }
        return resultComics ;
    }

    public static void main(String[] args) throws Exception {
        JDBCComicExtractor ce = new JDBCComicExtractor() ;
        int[] x = {1,2,3,4,5,6,7,8,9} ;
        for (Comic c : ce.getComicFromCopyIArray(x)) {
            System.out.println(c);
        }
    }

}
