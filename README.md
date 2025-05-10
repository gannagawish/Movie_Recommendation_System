
# 🎬 Movie Recommendation System

A simple Java-based Movie Recommendation System that suggests movies based on user preferences. This project demonstrates basic concepts of recommendation algorithms, data handling, and object-oriented programming in Java.

---

## 📌 Features

- Recommends movies based on:
  - User ratings
  - Genre preferences
  - Similar users (collaborative filtering)
- Simple and clean Java structure
- Easy to extend and modify

---

## 📂 Project Structure

```
MovieRecommendationSystem/
├── src/
│   ├── main/java/org/example/
│   │   ├── Main.java                  # Entry point of the program
│   │   ├── CodesCracker.java          # Loads and parses movie data
│   │   ├── InputHandling.java         # Parses and validates user input
│   │   ├── Movie.java                 # Movie class model
│   │   ├── Recommend.java             # Core recommendation logic
│   │   ├── RecommendationService.java# Orchestrates the recommendation system flow
│   │   ├── User.java                  # User class model
│   │   └── exceptions/                # Custom exceptions (TitleException, LetterException, etc.)
│
│   ├── test/java/org/example/
│   │   ├── UnitTesting/               # Unit tests for individual components
│   │   │   ├── CodesCrackerTest.java
│   │   │   ├── RecommendTest.java
│   │   │   ├── RecommendationServiceTest.java
│   │   │   └── UserTest.java
│   │   ├── WhiteBoxTesting/          # White-box tests organized by coverage type
│   │   │   ├── Branch/
│   │   │   │   └── InputHandlingTest.java
│   │   │   ├── DataFlow/
│   │   │   │   ├── GenreParserTest.java
│   │   │   │   └── IdCheckerTest.java
│   │   │   ├── Path/
│   │   │   │   └── TitleCheckerTests.java
│   │   │   └── Statment/
│   │   │       ├── CodesCrackerTest.java
│   │   │       ├── RecommendTest.java
│   │   │       └── UserTest.java
│   │   └── integration/              # Integration tests across components
│   │       ├── CodesCrackerMovieIntegrationTest.java
│   │       ├── InputHandlingUserIntegrationTest.java
│   │       ├── RecommendSystemIntegrationTest.java
│   │       └── RecommendationServiceIntegrationTest.java
│
├── META-INF/
├── movies.txt                        # Sample movie input file
├── users.txt                         # Sample user input file
├── recommendations.txt               # Output of the recommendation system
├── output.txt                        # Additional system output if any
└── README.md                         # Project description and setup instruction

```

---

## 🚀 How to Run

1. **Clone the repository**

```bash
git clone https://github.com/gannagawish/MovieRecommendationSystem.git
```

2. **Open in your Java IDE** (IntelliJ, Eclipse, NetBeans, etc.)

3. **Run `Main.java`**

---

## 🔍 Recommendation Logic

- **Content-Based Filtering**: Recommends similar movies based on genre and description.
- **Collaborative Filtering** (optional): Recommends movies based on user similarity.
- **Hybrid Approach** (if you combined both).

---

## 🛠 Technologies Used

- Java (OOP)
- CSV/JSON file reading
- Basic data structures (ArrayList, HashMap, etc.)

---
