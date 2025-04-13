package main.java.org.example;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;
import java.io.*;

class TitleException extends Exception {
    String title;
    TitleException(String title){
        this.title=title;
    }
}
class LetterException extends Exception {
    String id;
    LetterException(String id){
        this.id=id;
    }
}
class NumberException extends Exception {
    String id;
    NumberException(String id){
        this.id=id;
    }
}

public class CodesCracker
{

    public static Vector<Movie> scanner() throws TitleException,LetterException,NumberException {
        String myfile, myline;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter File Name: ");
        myfile = scan.nextLine();
        Vector<Movie> movies=new Vector<>();
        try
        {
            FileReader fileReader = new FileReader(myfile);
            BufferedReader bufread = new BufferedReader(fileReader);
            Set<String>IDNumbers=new HashSet<>();

            // reading the file, line by line
            while((myline = bufread.readLine()) != null){
                //System.out.println(myline);
                Movie movie=titleParser(myline,IDNumbers);
                myline = bufread.readLine();
                //System.out.println(myline);
                movie.setGenre(genreParser(myline));
                movies.add(movie);
            }
            bufread.close();
        }
        catch(IOException e)
        {
            System.out.println("Exception: " +e);
        }
        return movies;
    }
    public static Movie titleParser(String line,Set<String>IDNumbers) throws TitleException,LetterException,NumberException {
        line=line.trim();
        String str[]=line.split(",");
        //str[0] title
        str[0]=str[0].trim();
        String caps=titleChecker(str[0]);
        //str[1] id
        str[1]=str[1].trim();
        IDChecker(str[1],caps,IDNumbers);

        Movie movie = new Movie();
        movie.setTitle(str[0]);
        movie.setID(str[1]);
        return movie;
    }
    public static Vector<String> genreParser(String line){
        Vector<String>genre=new Vector<>();
        String str[]=line.split(",");
        for(String s:str){
            s=s.trim();
            genre.add(s);
        }
        return genre ;
    }
    public static String titleChecker(String title) throws TitleException {
        String caps="";
        String titleArray[]=title.split(" ");

        for(String s:titleArray){
            for(int i=0;i<s.length();i++){
                if(i==0){
                    caps += s.charAt(i);
                    if(Character.isLowerCase(s.charAt(i))){  //Character.isLowerCase(s.charAt(i))
                       throw (new TitleException(title));
                    }
                }
                else if(Character.isUpperCase(s.charAt(i))){  //Character.isUpperCase(s.charAt(i))
                        throw (new TitleException(title));
                }
            }

        }
        return caps;
    }
    public static void IDChecker(String id, String caps,Set<String>IDNumbers) throws LetterException,NumberException{
        String idNum=id.substring(caps.length());

       if(!id.substring(0, caps.length()).equals(caps)){
           throw new LetterException(id);
       }
       if(IDNumbers.contains(id.substring(caps.length())) || idNum.length()!=3){
           throw new NumberException(id);
        }
       else IDNumbers.add(idNum);

    }

}