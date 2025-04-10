//import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//
//public class Main {
//    public static void main(String[] args) {
//        String myfile, myline;
//        Scanner scan = new Scanner(System.in);
//
//        System.out.print("Enter File Name: ");
//        myfile = scan.nextLine();
//
//        List<User> users = new ArrayList<>(); // List to store valid User objects
//        InputHandling inputHandler = new InputHandling();
//
//        try (BufferedReader bufread = new BufferedReader(new FileReader(myfile))) {
//            // Check if the file is empty
//            File file = new File(myfile);
//            if (file.length() == 0) {
//                System.out.println("Warning: File '" + myfile + "' is empty.");
//                scan.close();
//                return;
//            }
//
//            // Read the file line by line
//            while ((myline = bufread.readLine()) != null) {
//                if (myline.trim().isEmpty()) {
//                    System.out.println("Skipping empty line.");
//                    continue; // Skip empty lines
//                }
//
//                // Parse the line using InputHandling
//                String[] parsedInput = inputHandler.parseInput(myline);
//                if (parsedInput == null) {
//                    // parseInput already printed an error message
//                    continue;
//                }
//
//                String name = parsedInput[0];
//                String id = parsedInput[1];
//
//                User user = new User(name, id); // Create a User object and validate
//                if (!user.verifyUserName(name)) {
//                    System.out.println("ERROR: User Name "+ name +" is wrong");
//                    break;
//                }
//
//                if (!user.verifyUniqueID(id)) {
//                    System.out.println("ERROR: User Id "+ id +" is wrong");
//                    break;
//                }
//
//                // If all validations pass, add the user to the list
//                users.add(user);
//                System.out.println("Successfully added: Name: " + name + ", Id: " + id);
//            }
//
//            // Print all stored users
//            if (users.isEmpty()) {
//                System.out.println("\nNo valid users found.");
//            } else {
//                System.out.println("\nStored Users:");
//                for (User user : users) {
//                    System.out.println("Name: " + user.getUserName() + ", Id: " + user.getUserID());
//                }
//            }
//
//        } catch (IOException e) {
//            System.out.println("Exception: " + e);
//        }
//
//        scan.close();
//    }
//}

//import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//public class Main {
//    public static void main(String[] args) {
//        String myfile, userLine, movieLine;
//        Scanner scan = new Scanner(System.in);
//
//        System.out.print("Enter File Name: ");
//        myfile = scan.nextLine();
//
//        List<User> users = new ArrayList<>();
//        InputHandling inputHandler = new InputHandling();
//
//        try (BufferedReader bufread = new BufferedReader(new FileReader(myfile))) {
//            // Check if the file is empty
//            File file = new File(myfile);
//            if (file.length() == 0) {
//                System.out.println("Warning: File '" + myfile + "' is empty.");
//                scan.close();
//                return;
//            }
//
//            // Read the file two lines at a time
//            while ((userLine = bufread.readLine()) != null) {
//                // Read the movie id line
//                movieLine = bufread.readLine();
//
//                // Check if userLine is empty
//                if (userLine.trim().isEmpty()) {
//                    System.out.println("Skipping empty user line.");
//                    if (movieLine != null && !movieLine.trim().isEmpty()) {
//                        System.out.println("Skipping movie id line: '" + movieLine + "' due to missing user data.");
//                    }
//                    continue;
//                }
//
//                // If movieLine is missing (e.g., end of file), handle the error
//                if (movieLine == null) {
//                    System.out.println("Error: Missing movie id line for user line: '" + userLine + "'.");
//                    break;
//                }
//
//                // If movieLine is empty, skip this entry
//                if (movieLine.trim().isEmpty()) {
//                    System.out.println("Skipping entry due to empty movie id line for user line: '" + userLine + "'.");
//                    continue;
//                }
//
//                // Parse the two lines using InputHandling
//                Object[] parsedInput = inputHandler.parseInput(userLine, movieLine);
//                if (parsedInput == null) {
//                    continue;
//                }
//
//                String name = (String) parsedInput[0];
//                String id = (String) parsedInput[1];
//                List<String> movieIDs = (List<String>) parsedInput[2];
//
//                User user = new User(name, id, movieIDs);
//                if (!user.verifyUserName(name)) {
//                    System.out.println("ERROR: User Name '" + name + "' is wrong");
//                    continue;
//                }
//
//                if (!user.verifyUniqueID(id)) {
//                    System.out.println("ERROR: User Id '" + id + "' is wrong");
//                    continue;
//                }
//
//                users.add(user);
//                System.out.println("Successfully added: Name: " + name + ", User Id: " + id + ", Movie IDs: " + movieIDs);
//            }
//
//            // Print all stored users
//            if (users.isEmpty()) {
//                System.out.println("\nNo valid users found.");
//            } else {
//                System.out.println("\nStored Users:");
//                for (User user : users) {
//                    System.out.println(user.getUserName()); // Uses toString() method
//                }
//            }
//
//        } catch (IOException e) {
//            System.out.println("Exception: " + e);
//        }
//
//        scan.close();
//    }
//}

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        String myfile, userLine, movieLine;
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter File Name: ");
        myfile = scan.nextLine();

        List<User> users = new ArrayList<>();
        InputHandling inputHandler = new InputHandling();

        // Open output.txt for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            try (BufferedReader bufread = new BufferedReader(new FileReader(myfile))) {
                // Check if the file is empty
                File file = new File(myfile);
                if (file.length() == 0) {
                    writer.write("Warning: File '" + myfile + "' is empty.\n");
                    scan.close();
                    return;
                }

                // Read the file two lines at a time
                while ((userLine = bufread.readLine()) != null) {
                    // Read the movie id line
                    movieLine = bufread.readLine();

                    // Check if userLine is empty
                    if (userLine.trim().isEmpty()) {
                        writer.write("Skipping empty user line.\n");
                        if (movieLine != null && !movieLine.trim().isEmpty()) {
                            writer.write("Skipping movie id line: '" + movieLine + "' due to missing user data.\n");
                        }
                        continue;
                    }

                    // If movieLine is missing (e.g., end of file), handle the error
                    if (movieLine == null) {
                        writer.write("Error: Missing movie id line for user line: '" + userLine + "'.\n");
                        break;
                    }

                    // If movieLine is empty, skip this entry
                    if (movieLine.trim().isEmpty()) {
                        writer.write("Skipping entry due to empty movie id line for user line: '" + userLine + "'.\n");
                        continue;
                    }

                    // Parse the two lines using InputHandling
                    Object[] parsedInput = inputHandler.parseInput(userLine, movieLine);
                    if (parsedInput == null) {
                        // InputHandling already prints parsing errors to console
                        continue;
                    }

                    String name = (String) parsedInput[0];
                    String id = (String) parsedInput[1];
                    List<String> movieIDs = (List<String>) parsedInput[2];

                    User user = new User(name, id, movieIDs);
                    if (!user.verifyUserName(name)) {
                        writer.write("ERROR: User Name '" + name + "' is wrong\n");
                        break;
                    }

                    if (!user.verifyUniqueID(id)) {
                        writer.write("ERROR: User Id '" + id + "' is wrong\n");
                        break;
                    }

                    users.add(user);
                    writer.write("Successfully added: Name: " + name + ", User Id: " + id + ", Movie IDs: " + movieIDs + "\n");
                }

                // Write all stored users to the file
                if (users.isEmpty()) {
                    writer.write("\nNo valid users found.\n");
                } else {
                    writer.write("\nStored Users:\n");
                    for (User user : users) {
                        writer.write(user.getUserName().toString() + "," + user.getUserID().toString() +"\n");
                    }
                }

            } catch (IOException e) {
                writer.write("Exception: " + e + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error writing to output.txt: " + e);
        }

        scan.close();
    }
}