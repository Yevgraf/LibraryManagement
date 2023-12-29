package Data;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;


public class DBconn {

    private static String url = "";
    private static String user = "";
    private static String pass = "";


    /**
     * Establishes a connection to the database.
     *
     * @return The database connection.
     */
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return conn;
    }
}
