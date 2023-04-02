/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Single-threaded program that finds all solutions to a sudoku board using a recursive
// backtracking method.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SingleThreadedDFS {
    // Array list to store solution(s)
    private static ArrayList<int[][]> solutions;

    // Helper method to prepare the sudoku board and solve it
    public static ArrayList<int[][]> solve(Board board) {
        // Initialize solutions ArrayList
        solutions = new ArrayList<>();

        // Solve the board
        recursiveSolve(board, 0);

        return solutions;
    }

    // Helper method to recursively solve the sudoku board using backtracking
    private static void recursiveSolve(Board board, int cell) {
        // Extrapolate current i and j values from current cell number (0-indexed)
        // e.g. for a 3x3 board, cell numbers are:
        //      0 1 2
        //      3 4 5
        //      6 7 8
        int at_i = cell / board.n;
        int at_j = cell % board.n;

        // If it's out of bounds, the board is solved so put the board in the solutions ArrayList and return
        if (at_i >= board.n || at_j >= board.n) {
            solutions.add(board.board);
            return;
        }

        // If nothing's possible in this cell, no solution possible from this point so return
        if (board.possible[at_i][at_j] == 0) return;
        
        // If there's already something in this cell, just go to the next cell
        if (board.board[at_i][at_j] != 0) recursiveSolve(board, cell + 1);

        // Go through each possible number to go in this cell
        for (int i = 1; i < board.n + 1; i++) {
            if ((board.possible[at_i][at_j] & (1 << i)) == (1 << i)) { // If the number is possible in this cell
                // Place it in the cell
                Board newBoard = board.tryPlacement(i, at_i, at_j);

                // Recurse from here
                recursiveSolve(newBoard, cell + 1);
            }
        }
    }
}
