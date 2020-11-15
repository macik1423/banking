package banking;

import java.sql.*;

public class DataBase {
    private static final String URL = "jdbc:sqlite:";
    private static String fullPath;

    public DataBase(String fileName) {
        fullPath = URL + fileName;
        connect(fullPath);
        createNewDatabase(fullPath);
        createNewTable(fullPath);
    }

    private void createNewDatabase(String fullPath) {
        try (Connection conn = DriverManager.getConnection(fullPath)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
//                System.out.println("The driver name is " + meta.getDriverName());
//                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void connect(String fullPath) {
        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(fullPath);

//            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void createNewTable(String fullPath) {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card ("
        + " id integer PRIMARY KEY AUTOINCREMENT,"
        + " number text,"
        + " pin text,"
        + " balance integer default 0"
        + ");";

        try (Connection conn = DriverManager.getConnection(fullPath);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getFullPath() {
        return fullPath;
    }

}
