package JDBChelpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import model.Comic;
import model.Creator;
import model.Publisher;
import model.Character;


class JDBCtest{
    public static void main(String[] args) {

        String sql = """
            SELECT
                comic_info_test.comic_id,
                publisher_info_test.publisher_id,
                publisher_info_test.publisher_name,
                comic_info_test.series,
                comic_info_test.title,
                comic_info_test.volume_num,
                comic_info_test.issue_num,
                comic_info_test.release_date,
                comic_info_test.descrip,
                comic_ownership_test.comic_value,
                comic_ownership_test.slabbed
            FROM 
                comic_info_test

            INNER JOIN publisher_info_test ON 
                publisher_info_test.publisher_id = comic_info_test.publisher_fk
            INNER JOIN comic_ownership_test ON
                comic_ownership_test.comic_fk = comic_info_test.comic_id

            WHERE comic_ownership_test.user_fk = 0
                
        """;
        // String sql_creators = """
        //     SELECT 
        //         creator_info_test.creator_id,
        //         creator_info_test.creator_name
        //     FROM
        //         creator_refrence_test

        //     INNER JOIN creator_info_test ON
        //         creator_refrence_test.creator_fk = creator_info_test.creator_id
            
        //     WHERE creator_refrence_test.comic_fk = ?
        // """;
        String url = "jdbc:postgresql://jdb1.c4qx1ly4rhvr.us-east-2.rds.amazonaws.com:5432/postgres";
        ArrayList<Comic> comix = new ArrayList<Comic>() ;

        try (
            Connection conn = DriverManager.getConnection(url, "swen262", "bubbles");
            Statement stmt = conn.createStatement()
        ) {

            try {
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int                     id                    = rs.getInt("comic_id");
                    ArrayList<Publisher>    publisher             = null ;
                    String                  series                = rs.getString("series") ;
                    String                  title                 = rs.getString("title") ;
                    int                     volumeNumber          = rs.getInt("volume_num") ;
                    String                  issueNumber           = rs.getString("issue_num") ;
                    String                  publicationDate       = rs.getString("release_date") ;
                    ArrayList<Creator>      creators              = null ;
                    ArrayList<Character>    principlCharacters    = null ;
                    String                  description           = rs.getString("descrip") ;
                    float                   initial_value         = 0f ;
                    float                   value                 = rs.getInt("comic_value") ;
                    int                     grade                 = 0 ;
                    boolean                 isSlabbed             = rs.getBoolean("slabbed") ;


                    Comic generated_comic = new Comic(
                                                    id, publisher, series, 
                                                    title, volumeNumber, issueNumber, 
                                                    publicationDate, creators, principlCharacters,
                                                    description, initial_value, value, grade, isSlabbed);
                    System.out.println(generated_comic);
                    comix.add(generated_comic) ;
                }
            } catch (PSQLException e ) {
                throw new Error("Problem", e);
            } 

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } 

        System.out.println(comix);
    }


}