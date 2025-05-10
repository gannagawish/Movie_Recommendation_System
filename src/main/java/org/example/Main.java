package main.java.org.example;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try {
            RecommendationService.runRecommendationSystem();
        } catch (IOException e) {
            System.out.println("Error writing to recommendations.txt: " + e);
        }
    }
}

