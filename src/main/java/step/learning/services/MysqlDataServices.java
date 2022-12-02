package step.learning.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDataServices implements step.learning.services.DataService{
    private Connection connection;
    private final String connectionString = "jdbc:mysql://localhost:3306/java191" +
            "?useUnicode=true&characterEncoding=UTF-8" ;
    private final String dbUser = "user191" ;
    private final String dbPass = "pass191" ;


    public Connection getConnection() {
        if(connection==null)
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection= DriverManager.getConnection(connectionString,dbUser,dbPass);
            } catch (SQLException e) {
                System.out.println("MysqlDataService::getConnection() - "+e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return  connection;
    }
}
