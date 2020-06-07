/*
 *      TicTacToe.java - Main program for the TicTacToe game - provides interactive user interface and game loops for AI and two player mode
 *
 *      Copyright 2020 Alex Mous
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.

 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.

 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.util.Scanner;

public class TicTacToe {
    static Scanner stdin;

    public static void main(String[] args) {
        Board board;
        boolean use_ai;
        System.out.println("--------------- Welcome to TicTacToe ---------------\n---------- A Command-Line Tic Tac Toe game ---------\n");
        stdin = new Scanner(System.in);

        //Set up board size
        System.out.print("Please enter the board size: ");
        while (true) {
            try {
                board = new Board(stdin.nextInt());
                break;
            } catch (Exception e) {
                System.out.println("Unsupported value. Must be a positive integer. Please try again: ");
            }
        }

        //Set up board mode
        System.out.print("Please a mode (0 for AI, 1 for two player): ");
        while (true) {
            try {
                use_ai = (stdin.nextInt() == 0);
                break;
            } catch (Exception e) {
                System.out.println("Unsupported value. Please enter either 0 or 1: ");
            }
        }

        if (use_ai) {
            if (board.getSize() >= 16) {
                System.out.println("Warning! Sizes above 3x3 could take a very long time to calculate.");
            }
            aiGameLoop(board, true);
        } else {
            twoPlayerGameLoop(board, true);
        }
        System.out.println("\n- - - - - - - - - Game over! - - - - - - - - -\n- - - - - - - - Player " + board.getWinner() + " won!- - - - - - - - -");
    }

    public static boolean twoPlayerGameLoop(Board board, boolean turn) { //True for user, false for AI. Returns true if the X won, false if O won
        if (!board.checkFinished()) {
            System.out.println("\n- - - - - - - - - - " + (turn ? "X" : "O") + " Turn - - - - - - - - - -");
            System.out.print(board);
            System.out.print("\tPlease enter a move (in the form row column): ");
            int x = stdin.nextInt();
            int y = stdin.nextInt();
            if (board.setSquare(x-1, y-1, (turn ? "X" : "O"))) {
                System.out.println("\t" + (turn ? "X" : "O") + " chooses " + x + " " + y);
            } else {
                System.out.println("\nERROR: Invalid values and/or already set. Please try again");
                return twoPlayerGameLoop(board, turn); //Shortcut by returning without initiating a turn change
            }
            return twoPlayerGameLoop(board, !turn);
        } else {
            return board.whoWon(); //Returns true for X, false for O
        }
    }

    public static boolean aiGameLoop(Board board, boolean turn) { //True for user, false for AI. Returns true if the X won, false if O won
        if (!board.checkFinished()) {
            if (turn) { //Users turn
                System.out.println("\n- - - - - - - - - - X Turn - - - - - - - - - -");
                System.out.print(board);
                System.out.print("\tPlease enter a move (in the form row column): ");
                int x = stdin.nextInt();
                int y = stdin.nextInt();
                if (board.setSquare(x-1, y-1, "X")) {
                    System.out.println("\tX chooses " + x + " " + y);
                } else {
                    System.out.println("\nERROR: Invalid values and/or already set. Please try again");
                    return aiGameLoop(board, true); //Shortcut by returning without initiating a turn change
                }
            } else {
                System.out.println("\n- - - - - - - - - - O Turn - - - - - - - - - -");
                Integer[] res = getMultithreadedMove(board);
                int row = res[0];
                int col = res[1];
                System.out.println("\tO chooses " + (row+1) + " " + (col+1));
                board.setSquare(row, col, "O");
            }
            return aiGameLoop(board, !turn);
        } else {
            return board.whoWon(); //Returns true for X, false for O
        }
    }



    public static Integer[] getMultithreadedMove(Board board) { //Returns the move in an x/y pair
        Integer[][] choices = board.openSlots();
        Integer[] choice = new Integer[2];
        Thread[] threads = new Thread[board.numberOpenSlots()];
        Minimax[] thread_f = new Minimax[board.numberOpenSlots()];

        for (int i=0; i<board.numberOpenSlots(); i++) { //Iterate over set of possible choices
            board.setSquare(choices[i][0], choices[i][1], "O");
            thread_f[i] = new Minimax(board.clone());
            Thread t = new Thread(thread_f[i]);
            threads[i] = t;
            t.start();
            board.clearSquare(choices[i][0], choices[i][1]);
        }

        int best = -1000;
        for (int i=0; i<board.numberOpenSlots(); i++) { //Iterate over set of possible choices
            try {
                threads[i].join();
                int r = thread_f[i].getResult();
                if (r > best) {
                    best = r;
                    choice = choices[i];
                }
            } catch (InterruptedException e) {
                //Do nothing
            }
        }

        return choice;
    }
}
