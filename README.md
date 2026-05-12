Snake Game:

A console-launched, Swing-rendered Snake game built in Java as part of a class project. Eat the food, grow your snake, and beat your high score — without hitting the walls or yourself.

How to Run:

Make sure you have Java JDK 21 installed
Clone the repository:

Git clone: https://github.com/jsgreenwell/SmallGamesProject

Open the project in IntelliJ IDEA
Navigate to Main.java
Right-click Main.java → Run 'Main.main()'
Enter your name in the console, then select Play Snake from the menu


How to Play:

KeyActionW or ↑Move UpS or ↓Move DownA or ←Move LeftD or →Move RightPPause / ResumeQQuitENTERStart / Restart

Eat the red food to grow and earn points
Every 5 foods eaten = level up and speed increase
Avoid hitting the walls or your own body
High scores are saved automatically to highscores.txt


Project Structure:

FileDescriptionMain.javaEntry point - shows the main menu and launches the gameDisplay.javaAll GUI rendering, colors, animations, and keyboard input using SwingGameBoard.javaGame grid, wall setup, and collision detectionSnake.javaSnake body, movement, growth, and direction logicsnakeFood.javaFood position tracking and random respawningFileHandler.javaSaves and loads high scores from highscores.txt

Authors
Name/Roles Blayne Ryherd GUI, Display, colors, FileHandler and Debugging - Rain Brown Snake movement and food - Aiden Simmons Game board and logic

License
This project is licensed under the MIT License - see the LICENSE file for details.