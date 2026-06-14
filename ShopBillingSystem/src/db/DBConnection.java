 package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        try {

            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=ShopManagementDB;user=sa;password=2005;encrypt=false;"
            );

            System.out.println("Database Connected");
            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}