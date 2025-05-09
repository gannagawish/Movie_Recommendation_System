package main.java.org.example;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;

import java.util.*;
import java.io.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Main
{

    public static void main(String[] args)
    {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
            CodesCracker scan2 = new CodesCracker();
            Vector<Movie> movies=new Vector<>();
            try {

                movies=scan2.scanner();
            }catch (TitleException title){
                writer.write("ERROR: Movie Title {"+title.title+"} is wrong\n");
                return;
            }
            catch(LetterException idLetter){
                writer.write("ERROR: Movie Id letters {"+idLetter.id+"} are wrong\n");
                return;
            }
            catch(NumberException idNumber){
                writer.write("ERROR: Movie Id numbers {"+idNumber.id+"} aren't unique\n");
                return;
            }

            //begin of reading of users
            System.out.print("Enter File Name: ");

            String myfile, userLine, movieLine;
            Scanner scan = new Scanner(System.in);
            myfile = scan.nextLine();

            List<User> users = new ArrayList<>();
            InputHandling inputHandler = new InputHandling();

            // Open output.txt for writing
            try (BufferedReader bufread = new BufferedReader(new FileReader(myfile))) {
                // Check if the file is empty
                File file = new File(myfile);
                if (file.length() == 0) {
                    scan.close();
                    return;
                }

                // Read the file two lines at a time
                while ((userLine = bufread.readLine()) != null) {
                    // Read the movie id line
                    movieLine = bufread.readLine();

                    // Parse the two lines using InputHandling
                    Object[] parsedInput = inputHandler.parseInput(userLine, movieLine);
                    if (parsedInput == null) {
                        // InputHandling already prints parsing errors to console
                        continue;
                    }

                    String name = (String) parsedInput[0];
                    String id = (String) parsedInput[1];
                    List<String> movieIDs = (List<String>) parsedInput[2];
                    Set<String>IDNumbers_Users=new HashSet<>();
                    for(String j:movieIDs){
                        String idNumUser="";
                        for(int i=0;i<j.length();i++) {
                            if (Character.isLowerCase(j.charAt(i))) {
                                writer.write("ERROR: Movie Id letters {" + j + "} are wrong\n");
                                return;
                            }
                            else if(Character.isDigit(j.charAt(i))){
                                idNumUser +=j.charAt(i);
                            }
                        }
                        if(IDNumbers_Users.contains(idNumUser) || idNumUser.length()!=3){
                            writer.write("ERROR: Movie Id numbers {"+j+"} aren't unique\n");
                            return;
                        }

                        else IDNumbers_Users.add(idNumUser);
                    }
                    User user = new User(name, id, movieIDs);
                    if (!user.verifyUserName(name)) {
                        writer.write("ERROR: User Name {" + name + "} is wrong\n");
                        return;
                    }

                    if (!user.verifyUniqueID(id)) {
                        writer.write("ERROR: User Id {" + id + "} is wrong\n");
                        return;
                    }
                    users.add(user);
                }
            } catch (IOException e) {
                writer.write("Exception: " + e + "\n");
            }
            scan.close();

            //end of users
            Recommend rr= new Recommend(users,movies);
            rr.createGenres();
            rr.recommendToUsers();
            rr.outputFile(writer);


        } catch (IOException e) {
            System.out.println("Error writing to recommendation.txt: " + e);
        }

    }
}

