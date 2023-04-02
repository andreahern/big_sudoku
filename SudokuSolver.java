/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Finds all solutions of a sudoku board and compares how long it took to solve using both
// single-threaded and multi-threaded methods.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SudokuSolver {
    // If given sudoku board as a file, parse it and turn into array
    public static long[] solve(String filename) {
        try {
            File file = new File(filename);
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
            // Otherwise initialize sqrtn
            int sqrtn = (int) Math.sqrt(n);

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
            return solve(board);
        } catch (FileNotFoundException e) {
            System.out.println("There was a problem opening the file.");
            e.printStackTrace();
            return null;
        }
    }

    public static long[] solve(int[][] board) {
        Board sudokuBoard = new Board(board);

        // Start the timer
        long startSingleThreaded = System.currentTimeMillis();

        // Solve the board using the single-threaded solver
        ArrayList<int[][]> singleThreadedSolutions = SingleThreadedSudokuSolver.solve(sudokuBoard);

        // End the timer
        long endSingleThreaded = System.currentTimeMillis();

        // Start the timer
        long startMultiThreaded = System.currentTimeMillis();

        // Solve the board using the multi-threaded solver
        ArrayList<int[][]> multiThreadedSolutions = MultiThreadedSudokuSolver.solve(sudokuBoard);

        // End the timer
        long endMultiThreaded = System.currentTimeMillis();

        if (!verifySolutions(singleThreadedSolutions, multiThreadedSolutions, board)) {
            System.out.println("Solution(s) not correct");
            return null;
        }

        long singleThreadedTime = endSingleThreaded - startSingleThreaded;
        long multiThreadedTime = endMultiThreaded - startMultiThreaded;
        return new long[] {singleThreadedTime, multiThreadedTime};
    }

    // Helper function to make sure solutions are correct
    private static boolean verifySolutions(ArrayList<int[][]> singleThreadedSolutions, ArrayList<int[][]> multiThreadedSolutions, int[][] board) {
        // Make sure all single-threaded solutions are accurate
        for (int[][] solution : singleThreadedSolutions) {
            if (!SolutionChecker.check(board, solution)) return false;
        }

        // Make sure there are the same number of solutions for both approaches
        if (singleThreadedSolutions.size() != multiThreadedSolutions.size()) return false;

        // If there are no solutions return true
        if (singleThreadedSolutions.size() == 0) return true;

        // Sort the solution lists
        Collections.sort(singleThreadedSolutions, new java.util.Comparator<int[][]>() {
            public int compare(int[][] a, int[][] b) {
                for (int i = 0; i < a.length; i++) {
                    for (int j = 0; j < a[0].length; j++) {
                        if (a[i][j] != b[i][j]) return Integer.compare(a[i][j], b[i][j]);
                    }
                }
                return 0;
            }
        });
        Collections.sort(multiThreadedSolutions, new java.util.Comparator<int[][]>() {
            public int compare(int[][] a, int[][] b) {
                for (int i = 0; i < a.length; i++) {
                    for (int j = 0; j < a[0].length; j++) {
                        if (a[i][j] != b[i][j]) return Integer.compare(a[i][j], b[i][j]);
                    }
                }
                return 0;
            }
        });

        // Make sure both approaches found the same solutions
        for (int i = 0; i < singleThreadedSolutions.size(); i++) {
            int n = singleThreadedSolutions.get(0).length;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (singleThreadedSolutions.get(i)[j][k] != multiThreadedSolutions.get(i)[j][k]) return false;
                }
            }
        }

        return true;
    }
}
