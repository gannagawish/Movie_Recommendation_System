//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main.java.org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
                CodesCracker scan2 = new CodesCracker();
                new Vector();

                Vector movies;
                try {
                    movies = CodesCracker.scanner();
                } catch (TitleException title) {
                    writer.write("ERROR: Movie Title {" + title.title + "} is wrong");
                    return;
                } catch (LetterException idLetter) {
                    writer.write("ERROR: Movie Id letters {" + idLetter.id + "} are wrong");
                    return;
                } catch (NumberException idNumber) {
                    writer.write("ERROR: Movie Id numbers {" + idNumber.id + "} aren't unique");
                    return;
                }

                System.out.print("Enter File Name: ");
                Scanner scan = new Scanner(System.in);
                String myfile = scan.nextLine();
                List<User> users = new ArrayList();
                InputHandling inputHandler = new InputHandling();

                try {
                    try (BufferedReader bufread = new BufferedReader(new FileReader(myfile))) {
                        File file = new File(myfile);
                        if (file.length() == 0L) {
                            scan.close();
                            return;
                        }

                        String userLine;
                        while((userLine = bufread.readLine()) != null) {
                            String movieLine = bufread.readLine();
                            Object[] parsedInput = inputHandler.parseInput(userLine, movieLine);
                            if (parsedInput != null) {
                                String name = (String)parsedInput[0];
                                String id = (String)parsedInput[1];
                                Set<String> IDNumbers_Users = new HashSet();
                                String idNumUser = "";

                                for(int i = 0; i < id.length(); ++i) {
                                    if (Character.isLowerCase(id.charAt(i))) {
                                        writer.write("ERROR: Movie Id letters {" + id + "} are wrong");
                                        return;
                                    }

                                    if (Character.isDigit(id.charAt(i))) {
                                        idNumUser = idNumUser + id.charAt(i);
                                    }
                                }

                                if (IDNumbers_Users.contains(idNumUser) || idNumUser.length() != 3) {
                                    writer.write("ERROR: Movie Id numbers {" + id + "} aren't unique");
                                    return;
                                }

                                IDNumbers_Users.add(id);
                                List<String> movieIDs = (List)parsedInput[2];
                                User user = new User(name, id, movieIDs);
                                if (!user.verifyUserName(name)) {
                                    writer.write("ERROR: User Name '" + name + "' is wrong\n");
                                    return;
                                }

                                if (!user.verifyUniqueID(id)) {
                                    writer.write("ERROR: User Id '" + id + "' is wrong\n");
                                    return;
                                }

                                users.add(user);
                            }
                        }
                    }
                } catch (IOException e) {
                    writer.write("Exception: " + String.valueOf(e) + "\n");
                }

                scan.close();
                Recommend rr = new Recommend(users, movies);
                rr.createGenres();
                rr.recommendToUsers();
                rr.outputFile(writer);
            }
        } catch (IOException e) {
            System.out.println("Error writing to recommendation.txt: " + String.valueOf(e));
        }
    }
}
