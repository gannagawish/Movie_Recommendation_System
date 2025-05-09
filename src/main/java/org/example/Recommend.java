package main.java.org.example;

import java.io.*;
import java.util.*;

public class Recommend {

    Map<String, Vector<String>> GenreMap=new HashMap<>();
    Map<User, HashSet<String>> RecommendsMap=new HashMap<>();
    Vector<Movie> movies=new Vector<>();
    List<User> users = new Vector<>();

    public void createGenres(){
        for(Movie m: movies){
            Vector<String>v= m.getGenre();
            for(String s:v){
                if(GenreMap.containsKey(s)){
                    GenreMap.get(s).add(m.getTitle());
                }
                else {
                    GenreMap.put(s, new Vector<>());
                    GenreMap.get(s).add(m.getTitle());
                }
            }
        }
    }
    public void recommendToUsers(){
        for(User u : users){
            RecommendsMap.put(u,new HashSet<>());
            for(String s: u.getMovieIDs()){   //ids of movies
                for(Movie m: movies) {
                    if(s.equals(m.getID())){
                        for(String g : m.getGenre()){   //genres
                            for( String moviesWithSameGenre:GenreMap.get(g)){
                                if(moviesWithSameGenre.equals(m.getTitle()))continue;
                                RecommendsMap.get(u).add(moviesWithSameGenre);
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<User, HashSet<String>> entry : RecommendsMap.entrySet()) {
            User key = entry.getKey(); //user
            HashSet<String> values = entry.getValue();
            for(String s: key.getMovieIDs()){  //movies of users
                for(Movie m: movies){  //movies kolh
                    if(m.getID().equals(s) && values.contains(m.getTitle())){
                        values.remove(m.getTitle());
                    }
                }
            }
        }
    }
    public void outputFile(BufferedWriter writer){
        try {
            for(Map.Entry<User, HashSet<String>> entry : RecommendsMap.entrySet()){
                User key = entry.getKey();
                HashSet<String> values = entry.getValue();
                writer.write(key.getUserName()+" "+key.getUserID()+"\n");
                int i=0;
                for(String v:values){
                    if(i<values.size()-1) {
                        writer.write(v + ", ");
                    }
                    else{
                        writer.write(v);
                    }
                    i++;
                }

                writer.write("\n");
            }

        } catch (IOException e) {
            System.out.println("Error writing to recommendation.txt: " + e);
        }
    }


    public Recommend(){}
    public Recommend(List<User> users, Vector<Movie> movies){
        this.movies=movies;
        this.users=users;
    }

    public Map<String, Vector<String>> getGenreMap() {
        return GenreMap;
    }

    public void setRecommendsMap(Map<User, HashSet<String>> recommendsMap) {
        RecommendsMap = recommendsMap;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public Map<User, HashSet<String>> getRecommendsMap() {
        return RecommendsMap;
    }

    public void setGenreMap(Map<String, Vector<String>> genreMap) {
        GenreMap = genreMap;
    }

    public Vector<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Vector<Movie> movies) {
        this.movies = movies;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }


}
