/*
 *      Minimax.java - algorithm that can run in a thread that will calculate the best score based on the board given
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

public class Minimax implements Runnable {
    Board b;
    int res;

    public Minimax(Board board) {
        this.b = board;
    }

    public void run() { //Run the minimax function
        this.res = minimax(b, true, 1, -1000, 1000);
    }

    public int getResult() { //Return the result
        return this.res;
    }

    public int minimax(Board board, boolean turn, int depth, int alpha, int beta) { //Searches until board full - turn (true is user, false is AI)
        if (!board.checkFinished()) { //Recursive case - get available choice and find the best/worst and return it
            int best = (turn ? 10 : -10);
            Integer[][] cases = board.openSlots();
            for (Integer[] c: cases) { //Iterate over set of possible choices
                if (c[0] != null) {
                    board.setSquare(c[0], c[1], turn ? "X" : "O");
                    int r = minimax(board, !turn, depth+1, alpha, beta);
                    if (turn) { //The user's turn - minimize the loss
                        best = Math.min(best, r);
                        beta = Math.min(beta, best);
                    } else { //The AI's turn - maximize the win
                        best = Math.max(best, r);
                        alpha = Math.max(alpha, best);
                    }
                    if (beta <= alpha) {
                        board.clearSquare(c[0], c[1]);
                        break;
                    }
                    board.clearSquare(c[0], c[1]);
                }
            }
            return best;
        } else { //Board full - return score
            if (board.getWinner() == null) { //Game tied
                return 0;
            } else if (board.getWinner().equals("X")) { //The user won - worst outcome
                return -1;
            } else if (board.getWinner().equals("O")) { //The AI won - best outcome
                return 1;
            }
        }
        return 0;
    }
}
