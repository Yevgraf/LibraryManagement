package Data;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;


public class DBconn {

    private static String url = "jdbc:sqlserver://ctespbd.dei.isep.ipp.pt;databaseName=2023_f_lp2_g5;TrustServerCertificate=True";
    private static String user = "2023_f_lp2_g5";
    private static String pass = "Equipa_vfr3";


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
