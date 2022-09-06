package course7.jukebox.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    public static Connection con;
    public ConnectionClass(){
        try
        {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/TheJukeBox", "root","Shweta@123");
        }
        catch (SQLException e)
        {
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
        }
    }
}
