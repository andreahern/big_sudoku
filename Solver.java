/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Program that checks if a sudoku board is valid and solves it.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Solver {
        public static void main(String[] args) {
        Board board = new Board(args[0]);  // The first argument is the name of the file that contains the board
        if (board == null) return;

        // Start the timer
        long start = System.currentTimeMillis();

        // Solve the board using the single-threaded solver
        ArrayList<int[][]> solutions = SingleThreadedSudokuSolver.solve(board);
        int numSolutions = solutions.size();

        // End the timer
        long end = System.currentTimeMillis();

        // Print result
        if (numSolutions == 0) System.out.println("Board is unsolvable");
        else {
            System.out.println("Solved in " + (end - start) + "ms");
            System.out.println((numSolutions == 1) ? ("There is 1 solution:") : ("There are " + numSolutions + " solutions:"));
            for (int i = 0; i < numSolutions; i++) {
                for (int j = 0; j < board.n; j++) {
                    for (int k = 0; k < board.n; k++) {
                        System.out.print(((solutions.get(i)[j][k] < 10) ? "0" : "") + solutions.get(i)[j][k] + ((k < board.n-1) ? " " : "\n"));
                    }
                }
                System.out.println();
            }
        }
    }
}
