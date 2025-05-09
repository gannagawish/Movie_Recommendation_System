
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
│   └── main/java/org/example/
│       ├── Main.java              # Entry point of the program
│       ├── Movie.java             # Movie class model
│       ├── User.java              # User class model
│       ├── Recommend.java         # Core recommendation logic
│       ├── InputHandling.java     # Handles parsing and validation
│       └── exceptions/            # Custom exceptions like TitleException, LetterException, etc.
│
├── test/
│   └── java/org/example/
│       ├── UnitTesting/           # Unit tests (e.g., InputHandlingTest, MovieTest)
│       ├── WhiteBoxTesting/       # For coverage and white-box analysis
│       └── integration/           # Integration tests across components
│
├── README.md
├── movies.txt
├── users.txt
├── recommendations.txt
└── output.txt

```

---

## 🚀 How to Run

1. **Clone the repository**

```bash
git clone https://github.com/your-username/MovieRecommendationSystem.git
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
