/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Single-threaded program that solves a sudoku board using a recursive backtracking method.
// If there is a solution, it prints the completed board and the time (in ms) it took to calculate.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SingleThreadedSudoku {
    // Array to store the solution, if it exists
    public static int[][] solution;

    public static void main(String[] args) {
        // Initialize n and board outside of try/catch
        int n = 0;
        int[][] board = null;

        // Start the timer
        long start = System.currentTimeMillis();

        // Read in the board from a file
        try {
            File file = new File(args[0]); // The first argument is the name of the file that contains the board
            Scanner scanner = new Scanner(file);

            // Take in the first line to find out the side length of the board
            String row = scanner.nextLine();
            // If first row is empty, quit
            if (row == null) {
                System.out.println("File is empty");
                return;
            }
            String[] rowSplit = row.split(" ");
            n = rowSplit.length;

            // Quit if n is not a perfect square
            if ((int) Math.sqrt(n) != Math.sqrt(n)) {
                System.out.println("Sudoku board is invalid");
                return;
            }

            // Initialize new board array
            board = new int[n][n];

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
                    return;
                }
                rowSplit = row.split(" ");

                // If column has too few columns, quit
                if (rowSplit.length < n) {
                    System.out.println("Sudoku has too few columns in one of the rows");
                    return;
                }
                // If column has too many columns, quit
                if (rowSplit.length < n) {
                    System.out.println("Sudoku has too many columns in one of the rows");
                    return;
                }

                // Copy the line to the array
                for (int j = 0; j < n; j++) {
                    if (rowSplit[j].charAt(0) != '_') board[i][j] = Integer.parseInt(rowSplit[j]);
                    else board[i][j] = 0;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("There was a problem opening the file.");
            e.printStackTrace();
        }

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

        // Initialize solution array
        solution = new int[n][n];

        // Solve the board
        boolean solved = solve(n, board, 0, allowed);

        // End the timer
        long end = System.currentTimeMillis();

        // Print result
        if (solved) {
            System.out.println("Solved in " + (end - start) + "ms");
            System.out.println("Solution:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n-1; j++) {
                    System.out.print(solution[i][j] + " ");
                }
                System.out.println(solution[i][n-1]);
            }
        }
        else System.out.println("Board is unsolvable");
    }

    // Helper function to fill the bitmask array with the numbers allowed in each cell
    public static void prepareBoard(int n, int[][] board, int[][] allowed) {
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
    public static boolean solve(int n, int[][] board, int cell, int[][] allowed) {
        // Extrapolate current i and j values from current cell number
        int at_i = cell / n;
        int at_j = cell % n;

        // If nothing's allowed in this cell, return false
        if (allowed[at_i][at_j] == 0) return false;
        // If there's already something in this cell, just go to the next cell
        if (board[at_i][at_j] != 0) {
            // Unless it's the last cell, in which case the board is solved so copy the board to the solution array and return true
            if (cell == (n * n) - 1) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        solution[i][j] = board[i][j];
                    }
                }
                return true;
            }
            return solve(n, board, cell + 1, allowed);
        }

        boolean solved = false;

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
                solved |= solve(n, boardNow, cell++, allowedNow);
            }
        }

        return solved;
    }
}
