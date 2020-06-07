# TicTacToe
An unbeatable Tic Tac Toe command-line game with any board size made in Java. Also has a two-player mode.

## Usage
Run the code with `java -jar TicTacToe.jar` (or via the scripts provided as detailed in the section __Batch and Shell Scripts__ below) and follow the on-screen instructions to set the board size and choose either two player mode or AI mode.

_Note:_ the AI will take a potentially significant amount of time if you select board sizes above 3x3! This program uses the minimax algorithm with alpha beta pruning and multithreading to search the game to the end, therefore making the game unbeatable.

### Batch and Shell Scripts
These two scripts provide a shortcut for opening the game on Windows and Macintosh, respectively. Simply double click on the Batch file on Windows or the sh file on Macintosh and the program will open in a terminal.

### Source Code
The source code for this project is available in the src folder. Feel free to use and implement this code in your own projects - this project is licensed under the GNU GPL.
