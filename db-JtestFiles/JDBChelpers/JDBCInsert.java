package JDBChelpers;

public class JDBCInsert {

    private final String URL ;
    private final String USER ;
    private final String PASS ;

    public JDBCInsert() {
        this.URL = "jdbc:postgresql://jdb1.c4qx1ly4rhvr.us-east-2.rds.amazonaws.com:5432/postgres" ;
        this.USER = "swen262" ;
        this.PASS = "bubbles" ;

        
    } public JDBCInsert( String url, String username, String password) {
        this.URL = url ;
        this.USER = username ;
        this.PASS = password ;
    }

}
