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
        long startSingleThreadedDFS = System.currentTimeMillis();
        // Solve the board using the single-threaded DFS solver
        ArrayList<int[][]> singleThreadedDFSSolutions = SingleThreadedDFS.solve(sudokuBoard);
        // End the timer
        long endSingleThreadedDFS = System.currentTimeMillis();

        // Start the timer
        long startSingleThreadedBFS = System.currentTimeMillis();
        // Solve the board using the single-threaded BFS solver
        ArrayList<int[][]> singleThreadedBFSSolutions = SingleThreadedDFS.solve(sudokuBoard);
        // End the timer
        long endSingleThreadedBFS = System.currentTimeMillis();

        // Start the timer
        long startMultiThreadedBFS = System.currentTimeMillis();
        // Solve the board using the multi-threaded BFS solver
        ArrayList<int[][]> multiThreadedBFSSolutions = MultiThreadedBFS.solve(sudokuBoard);
        // End the timer
        long endMultiThreadedBFS = System.currentTimeMillis();

        // Make comparator to sort solutions
        Comparator<int[][]> sort2DArrays = new java.util.Comparator<int[][]>() {
            public int compare(int[][] a, int[][] b) {
                for (int i = 0; i < a.length; i++) {
                    for (int j = 0; j < a[0].length; j++) {
                        if (a[i][j] != b[i][j]) return Integer.compare(a[i][j], b[i][j]);
                    }
                }
                return 0;
            }
        };

        // Sort the solution lists
        Collections.sort(singleThreadedDFSSolutions, sort2DArrays);
        Collections.sort(singleThreadedBFSSolutions, sort2DArrays);
        Collections.sort(multiThreadedBFSSolutions, sort2DArrays);

        // Verify all solution lists with each other
        if (!verifySolutions(singleThreadedDFSSolutions, singleThreadedBFSSolutions, board)) {
            System.out.println("Solution(s) not correct");
            return null;
        }
        if (!verifySolutions(singleThreadedDFSSolutions, multiThreadedBFSSolutions, board)) {
            System.out.println("Solution(s) not correct");
            return null;
        }

        long singleThreadedDFSTime = endSingleThreadedDFS - startSingleThreadedDFS;
        long singleThreadedBFSTime = endSingleThreadedBFS - startSingleThreadedBFS;
        long multiThreadedBFSTime = endMultiThreadedBFS - startMultiThreadedBFS;
        return new long[] {singleThreadedDFSTime, singleThreadedBFSTime, multiThreadedBFSTime};
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
