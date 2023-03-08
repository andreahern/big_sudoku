/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Program that checks if a sudoku board is valid and solves it.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Solver {
        public static void main(String[] args) {
        int[][] board = getBoard(args[0]);  // The first argument is the name of the file that contains the board
        if (board == null) return;
        int n = board.length;

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
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        System.out.print(((solutions.get(i)[j][k] < 10) ? "0" : "") + solutions.get(i)[j][k] + ((k < n-1) ? " " : "\n"));
                    }
                }
                System.out.println();
            }
        }
    }

    // Helper function to read in the sudoku board from a file
    private static int[][] getBoard(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            // Take in the first line to find out the side length of the board
            String row = scanner.nextLine();
            // If first row is empty, quit
            if (row == null) {
                System.out.println("File is empty");
                return null;
            }
            String[] rowSplit = row.split(" ");
            int n = rowSplit.length;

            // Quit if n is not a perfect square
            if ((int) Math.sqrt(n) != Math.sqrt(n)) {
                System.out.println("Sudoku board is invalid");
                return null;
            }

            // Initialize new board array
            int[][] board = new int[n][n];

            // Copy the first line to the array
            for (int i = 0; i < n; i++) {
                if (rowSplit[i].charAt(0) != '_') board[0][i] = Integer.parseInt(rowSplit[i]);
                else board[0][i] = 0;
            }

            // Populate the rest of the array with data from the file
            for (int i = 1; i < n; i++) {
                row = scanner.nextLine();
                // If row is empty, quit
                if (row == null) {
                    System.out.println("Sudoku has too few rows");
                    return null;
                }
                rowSplit = row.split(" ");

                // If column has too few columns, quit
                if (rowSplit.length < n) {
                    System.out.println("Sudoku has too few columns in one of the rows");
                    return null;
                }
                // If column has too many columns, quit
                if (rowSplit.length < n) {
                    System.out.println("Sudoku has too many columns in one of the rows");
                    return null;
                }

                // Copy the line to the array
                for (int j = 0; j < n; j++) {
                    if (rowSplit[j].charAt(0) != '_') board[i][j] = Integer.parseInt(rowSplit[j]);
                    else board[i][j] = 0;
                }
            }
            scanner.close();

            return board;

        } catch (FileNotFoundException e) {
            System.out.println("There was a problem opening the file.");
            e.printStackTrace();
            return null;
        }
    }
}
