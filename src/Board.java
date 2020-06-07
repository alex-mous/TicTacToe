/*
 *      Board.java - Data structure to hold the board representation and functions such as determining if the game is over and who won
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


public class Board implements Cloneable {
    private String[] squares;
    private final int rc; //Width and height
    private String winner; //Flag if board is complete

    public Board(int rc) {
        this.rc = rc;
        squares = new String[rc*rc];
    }

    public boolean setSquare(int row, int col, String v) { //Set the square at row,col with the value v
        if (row >= 0 && col >= 0 && row < rc && col < rc && (v.equals("X") || v.equals("O"))) {
            if (squares[row+(rc*col)] == null) {
                squares[row+(rc*col)] = v;
                return true;
            }
        }
        return false;
    }

    public Board clone() { //Return a copy of the board
        Board nb = new Board(this.rc);
        System.arraycopy(this.squares, 0, nb.squares, 0, squares.length);
        nb.checkFinished();
        return nb;
    }

    public void clearSquare(int row, int col) { //Clear the square at row,col
        if (row >= 0 && col >= 0 && row < rc && col < rc) {
            squares[row+(rc*col)] = null;
        }
    }

    public boolean isFull() { //Return if the board is full
        return (openSlots()[0][0] == null); //If the first slot is null, than there are no openings left
    }

    public boolean checkFinished() { //Return is the game is over or not
        for (int i=0; i<rc*rc; i+=rc) { //Horizontal
            if (squares[i] != null) {
                winner = squares[i];
                for (int j=i; j<i+rc; j++) {
                    if (squares[j] == null || !squares[i].equals(squares[j])) {
                        winner = null;
                        break;
                    }
                }
                if (winner != null) {
                    return true;
                }
            }
        }

        for (int i=0; i<rc; i+=1) { //Vertical
            if (squares[i] != null) {
                winner = squares[i];
                for (int j=i; j<rc*rc; j+=rc) {
                    if (squares[j] == null || !squares[i].equals(squares[j])) {
                        winner = null;
                        break;
                    }
                }
                if (winner != null) {
                    return true;
                }
            }
        }

        if (squares[0] != null) { //Left-slanting diagonal
            winner = squares[0];
            for (int i=0; i<rc*rc; i+=rc+1) {
                if (squares[i] == null || !squares[0].equals(squares[i])) {
                    winner = null;
                    break;
                }
            }
            if (winner != null) {
                return true;
            }
        }

        if (squares[rc-1] != null) { //Right-slanting diagonal
            winner = squares[rc-1];
            for (int i=rc-1; i<rc*rc-1; i+=rc-1) {
                if (squares[i] == null || !squares[rc-1].equals(squares[i])) {
                    winner = null;
                    break;
                }
            }
            if (winner != null) {
                return true;
            }
        }

        return this.isFull(); //No winner, but game full so a tie
    }

    public boolean whoWon() { //Return who won - true for X, false for O
        if (winner != null) {
            return winner.equals("X");
        }
        throw new UnsupportedOperationException("Game must be over before you can check who won!");
    }

    public String getWinner() { //Return the winner
        return this.winner;
    }

    public String toString() { //Return the board representation
        String res = "";
        res += underscoreRow();
        for (int col=0; col<rc; col++) {
            res += "\t| " + (squares[col] != null ? squares[col] : " ");
            for (int row=1; row<rc; row++) {
                res += " | " + (squares[col+rc*row] != null ? squares[col+rc*row] : " ");
            }
            res += " |\n" + underscoreRow();
        }
        return res + "\n";
    }

    private String underscoreRow() { //Return a row of dashes (used for toString)
        String res ="\t";
        for (int i=0; i<=rc*4; i++) {
            res += "-";
        }
        res += "\n";
        return res;
    }

    public Integer[][] openSlots() { //Returns an array of row/col pairs of open spots in the board
        Integer[][] res = new Integer[rc*rc][2];
        int c = 0; //Counter for last value in res
        for (int row=0; row<rc; row++) {
            for (int col=0; col<rc; col++) {
                if (squares[row+rc*col] == null) {
                    res[c] = new Integer[]{row,col};
                    c++;
                }
            }
        }
        return res;
    }

    public int numberOpenSlots() { //Return the number of open slots on the board
        int res = 0;
        for (Integer[] slot : openSlots()) {
            if (slot[0] != null) {
                res++;
            } else {
                break;
            }
        }
        return res;
    }

    public int getSize() { //Return the number of squares on the board
        return rc*rc;
    }
}
