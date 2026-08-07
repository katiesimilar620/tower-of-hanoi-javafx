# Tower of Hanoi

A desktop Tower of Hanoi game built with Java and JavaFX, featuring user accounts, save/load slots, an auto-solve visualizer, and a star-based scoring system at the end of each game.

## Overview

Beyond the classic puzzle mechanics, this isn't just a single-session game — it has a full account system so players can sign up, log in, and save their progress across multiple slots. Each completed game is scored with a 1–3 star rating based on how close the player's move count and completion time were to the mathematically optimal solution.

## Features

- **Disk dragging with move validation** — only legal moves (smaller disk onto a larger one, or an empty peg) are allowed
- **Auto-solve mode** — recursively computes and animates the optimal solution, with a Stop button to interrupt playback mid-solve
- **User accounts** — sign up, log in, and a "forgot password" flow, with passwords salted and hashed (PBKDF2) rather than stored in plaintext
- **Save/Load system** — 3 save slots per user, so multiple games can be kept in progress at once
- **Scoring system** — after each game, moves and time are compared against the theoretical optimum (`2^n - 1` moves) to award up to 3 stars
- **Sound effects** — move and win sound cues
- **Move & timer tracking** — live move counter and elapsed-time display during play

## Tech Stack

- **Language:** Java
- **UI Framework:** JavaFX (FXML + CSS for styling)
- **Build tool:** Maven

## Project Structure

```
tower-of-hanoi-javafx/
├── src/main/java/hanoi/towerofhanoi/
│   ├── Main.java                    # Application entry point
│   ├── Moderator.java               # Shared game state / navigation between screens
│   ├── User.java                    # User account model (Serializable)
│   └── controllers/
│       ├── HanoiController.java     # Core gameplay logic (drag/drop, moves, auto-solve)
│       ├── LoginPageController.java
│       ├── SignUpPageController.java
│       ├── ForgetPasswordController.java
│       ├── ProfilePageController.java
│       ├── SavePageController.java
│       ├── SaveSlot.java
│       ├── DiskCountController.java # Disk-count selection before starting a game
│       ├── EndGameController.java   # Post-game scoring screen
│       └── HomeController.java
├── src/main/resources/hanoi/towerofhanoi/
│   ├── *.fxml                       # Screen layouts
│   └── styles/*.css                 # Per-screen styling
└── files/                           # Sound assets + local save data (gitignored)
```

## How to Run

1. Make sure you have **JDK 17+** and **Maven** installed
2. Clone this repository
3. Run with Maven:
   ```
   ./mvnw clean javafx:run
   ```
   (or `mvnw.cmd clean javafx:run` on Windows)

## Note on Save Data

User accounts and save data are stored locally in `files/Users.dat` using Java serialization. This file is intentionally excluded from the repository (`.gitignore`) since it's local test data — passwords are salted and hashed with PBKDF2 (`javax.crypto`) before being stored, never kept in plaintext.

## Key Takeaways

Building the auto-solve visualizer meant separating the recursive Hanoi algorithm itself from its animation playback — computing the full move list first, then stepping through it on a timer so it could be paused/stopped mid-animation. Building the account and save-slot system was also a good exercise in structuring a multi-screen JavaFX app with shared state across controllers.

## Contributors

- [Mohammadreza Osuli](https://github.com/MoRe-O)
- [Hossein Sabzforoush](https://github.com/HosseinSabzforoush)
