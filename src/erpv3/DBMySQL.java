package erpv3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBMySQL {
    
    private final String URL = "jdbc:mysql://192.168.0.169:3306/erpdb";
    private final String USERNAME = "erpdbusr";
    private final String PASSWORD = "U><er!!!123";

    private Connection connection;

    public DBMySQL() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
}
