package course7.jukebox.userlogin;

import course7.jukebox.main.DisplayChoice;

import java.text.ParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {
    User user = new User();
    DisplayChoice displayChoice = new DisplayChoice();
    Scanner sc = new Scanner(System.in);

    public void gotoChoice(int choice) throws ParseException {
        if (choice == 1) {
            System.out.println("Enter Your Name.");
            String user_Name = sc.nextLine();
            while (!validateUserName(user_Name)) {
                System.out.println("Invalid UserName!");
                System.out.println("Please enter the Valid User Name.");
                user_Name = sc.nextLine();
                user.setUserName(user_Name);
            }
            user.setUserName(user_Name);

            System.out.println("Enter Password.");
            String password = sc.nextLine();
            while (!validatePassword(password)) {
                System.out.println("Invalid Password!");
                System.out.println("Please enter valid User Password.");
                password = sc.nextLine();
                user.setUserPassword(password);
            }
            System.out.println("Confirmed Password.");
            String password2 = sc.nextLine();
            if (password.equals(password2)) {
                user.setUserPassword(password);
            } else {
                System.out.println("Your Passwords are not same. please conformed again.");
                password2 = sc.nextLine();
                if (password.equals(password2)) {
                    user.setUserPassword(password);
                }
            }

            System.out.println("Enter your email Address");
            String email = sc.nextLine();
            while (!validateEmail(email)) {
                System.out.println("Invalid Email.");
                System.out.println("Please enter valid user Address.");
                email = sc.nextLine();
                user.setUserEmail(email);
            }
            user.setUserEmail(email);

            System.out.println("Enter your Mobile number.");
            String mobileNo = sc.nextLine();
            while (!validateMobile(mobileNo)) {
                System.out.println("Invalid Mobile Number.");
                System.out.println("Enter valid user Mobile.");
                mobileNo = sc.nextLine();
                user.setUserMobileNo(mobileNo);
            }
            user.setUserMobileNo(mobileNo);

            user.addUsers(user.getUserName(), user.getUserPassword(), user.getUserEmail(), user.getUserMobileNo());
            displayChoice.displayChoice1();

        }
        else if (choice == 2) {
            displayChoice.displayChoice2();
        }
        else {
            System.out.println("Please Enter 1 or 2");
            choice = sc.nextInt();
            gotoChoice(choice);
        }
    }

    // validate User Name
    boolean validateUserName(String userName) {
        if (userName == null) {
            return false;
        }
        // userName only contain capital and small letters
        // minimum length should be 4
        String regex = "[a-zA-Z]{4,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    // validate Password
    boolean validatePassword(String str) {
        if (str == null) {
            return false;
        }
        // first letter must be small or capital
        // must contain special character @$!%*?& and numbers
        // minimum length should be 8 and maximum 20
        String regex = "[a-zA-Z][a-zA-Z\\d@$!%*?&]{7,20}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    // validate email
    boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        // first letter must be capital or small letter
        // must contain minimum 4 letter or digit before @
        // after @ must contain minimum 3 letters or numbers
        // after . required 3 letters and . can have one or more
        // abcd@gamil.co.in is valid
        String regex = "[a-zA-z][a-zA-Z\\d._]{4,}@[a-zA-Z\\d]{3,}[\\\\.a-zA-Z{2,}]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validate mobile number
    boolean validateMobile(String mobileNo) {
        if (mobileNo == null) {
            return false;
        }
        // first digit must be 7,8,9 and maximum 10 digits
        String regex = "[7-9]\\d{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobileNo);
        return matcher.matches();
    }
}