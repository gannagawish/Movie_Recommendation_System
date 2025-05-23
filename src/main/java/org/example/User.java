package main.java.org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String userName;
    private String userID;
    private List<String> movieIDs;
    public static Set<String> userIDSet = new HashSet<>();


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMovieIDs(List<String> movieIDs) {
        this.movieIDs = movieIDs;
    }

    public String getUserName() {
        return userName;
    }

    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public List<String> getMovieIDs() {
        return movieIDs;
    }

    public User(String userName, String userID,List<String> movieIDs){
        this.userName = userName;
        this.userID = userID;
        this.movieIDs = movieIDs != null ? movieIDs : new ArrayList<>();
    }

    public boolean verifyUserName(String userName){
        String regex = "^[a-zA-Z][a-zA-Z ]*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public boolean verifyUserID(String userID){

        if(userID.length() != 9){
            return false;
        }
        String regex = "^[0-9][0-9A-Za-z]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    public boolean verifyUniqueID(String userID) {
        if (verifyUserID(userID)) {
            if (!userIDSet.contains(userID)) {
                userIDSet.add(userID);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}