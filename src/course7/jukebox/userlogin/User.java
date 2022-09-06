package course7.jukebox.userlogin;

import course7.jukebox.connection.ConnectionClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    //Attributes
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userMobileNo;

    public User(){}

    // getter and setter
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserMobileNo() {
        return userMobileNo;
    }
    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    // Method to add new User
    public void addUsers(String userName, String userPassword, String userEmail, String userMobileNo){
        int count;
        int userId = generateUserId() + 1;
        try {
            String sql = "insert into Users values(" + userId + ", '" + userName + "', '" +
                    userPassword + "', '" + userEmail + "', '" + userMobileNo + "')";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            userId = generateUserId();
            System.out.println("Hi " + userName + "\nYour UserId is " + userId);
            if(count == 0){
                System.out.println("No User added into database Users Table.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    // Method to check existing user
    public int checkForExistingUser(String userName, String userPassword){
        int User_Id = 0;
        try {
            String sql = "select User_Id from Users where user_Name = '" + userName + "' and User_Password = '"
                    + userPassword + "'";
            //System.out.println(sql);
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                User_Id = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return User_Id;
    }

    // method to get Max userId from users table
    private int generateUserId() {
        int userId  = 0;
        try {
            String sql = "select max(User_Id) from Users";
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                userId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}