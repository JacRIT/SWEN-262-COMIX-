package JDBChelpers;

public abstract class JDBC {
    
    protected final String URL ;
    protected final String USER ;
    protected final String PASS ;

    public JDBC() {
        this.URL = "jdbc:postgresql://jdb1.c4qx1ly4rhvr.us-east-2.rds.amazonaws.com:5432/postgres" ;
        this.USER = "swen262" ;
        this.PASS = "bubbles" ;

        
    } public JDBC( String url, String username, String password) {
        this.URL = url ;
        this.USER = username ;
        this.PASS = password ;
    }

    abstract public void executeSQL(String SQL) ;
}
