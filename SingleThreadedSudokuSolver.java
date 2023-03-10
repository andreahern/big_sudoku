/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Single-threaded program that finds all solutions to a sudoku board using a recursive
// backtracking method.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SingleThreadedSudokuSolver {
    // Array to store the solution, if it exists
    private static ArrayList<int[][]> solutions;

    // Helper method to prepare the sudoku board and solve it
    public static ArrayList<int[][]> solve(int[][] board) {
        int n = board.length;

        // Initialize solutions ArrayList
        solutions = new ArrayList<>();

        // Create an array of bitmasks to store the numbers allowed in each cell
        int[][] allowed = new int[n][n];
        // Fill with bitmasks of all 1s, initially assume all numbers are possible
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                allowed[i][j] = (1 << n+1) - 1;
            }
        }
        // Populate bitmasks with actual numbers allowed in each cell
        prepareBoard(n, board, allowed);

        // Solve the board
        recursiveSolve(n, board, 0, allowed);

        return solutions;
    }

    // Helper function to fill the bitmask array with the numbers allowed in each cell
    private static void prepareBoard(int n, int[][] board, int[][] allowed) {
        int sqrt = (int) Math.sqrt(n); // n is already guaranteed to be perfect square

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) { // If a cell is already populated
                    // Fill in bitmasks in row/column to not include this number
                    for (int k = 0; k < n; k++) {
                        allowed[k][j] &= ~(1 << board[i][j]);
                        allowed[i][k] &= ~(1 << board[i][j]);
                    }
                    // Fill in bitmasks in square to not include this number
                    for (int k = (i/sqrt)*sqrt; k < ((i/sqrt) + 1)*sqrt; k++) {
                        for (int l = (j/sqrt)*sqrt; l < ((j/sqrt) + 1)*sqrt; l++) {
                            allowed[k][l] &= ~(1 << board[i][j]);
                        }
                    }
                }
            }
        }
    }

    // Helper method to recursively solve the sudoku board using backtracking
    private static void recursiveSolve(int n, int[][] board, int cell, int[][] allowed) {
        // Extrapolate current i and j values from current cell number
        int at_i = cell / n;
        int at_j = cell % n;

        // If it's out of bounds, the board is solved so put the board in the solutions ArrayList and return
        if (at_i >= n || at_j >= n) {
            solutions.add(board);
            return;
        }

        // If nothing's allowed in this cell, no solution possible from this point so return
        if (allowed[at_i][at_j] == 0) return;
        // If there's already something in this cell, just go to the next cell
        if (board[at_i][at_j] != 0) recursiveSolve(n, board, cell + 1, allowed);

        // Go through each possible number to go in this cell
        for (int i = 1; i < n+1; i++) {
            int sqrt = (int) Math.sqrt(n); // n is already guaranteed to be perfect square

            if ((allowed[at_i][at_j] & (1 << i)) == (1 << i)) { // If the number is allowed in this cell
                // Make a copy of the board array
                int[][] boardNow = new int[n][n];
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        boardNow[j][k] = board[j][k];
                    }
                }

                // Make a copy of the allowed array
                int[][] allowedNow = new int[n][n];
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        allowedNow[j][k] = allowed[j][k];
                    }
                }

                // Put number in the cell in the new board array
                boardNow[at_i][at_j] = i;

                // Remove the number from the bitmasks in the row/column in the new allowed array
                for (int j = 0; j < n; j++) {
                    allowedNow[j][at_j] &= ~(1 << i);
                    allowedNow[at_i][j] &= ~(1 << i);
                }
                // Remove the number from the bitmasks in the square in the new allowed array
                for (int j = (at_i/sqrt)*sqrt; j < ((at_i/sqrt) + 1)*sqrt; j++) {
                    for (int k = (at_j/sqrt)*sqrt; k < ((at_j/sqrt) + 1)*sqrt; k++) {
                        allowedNow[j][k] &= ~(1 << i);
                    }
                }

                // Recurse from here
                recursiveSolve(n, boardNow, cell++, allowedNow);
            }
        }
    }
}
