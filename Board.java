/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Board object that stores a sudoku board and all possibilities for each empty space
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Board {
    public int n, sqrtn;
    public int[][] board, possible;

    // Constructor given a file name
    public Board(String filename) {
        // Parse board from file
        getBoard(filename);

        // Prepare possibilities array
        preparePossibilitiesArray();
    }

    // Constructor given components
    private Board(int n, int[][] board, int[][] possible) {
        this.n = n;
        this.sqrtn = (int) Math.sqrt(n);
        this.board = board;
        this.possible = possible;
    }

    // Helper function to read in the sudoku board from a file
    private int[][] getBoard(String fileName) {
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
            n = rowSplit.length;

            // Quit if n is not a perfect square
            if ((int) Math.sqrt(n) != Math.sqrt(n)) {
                System.out.println("Sudoku board is invalid");
                return null;
            }
            // Otherwise initialize sqrtn
            sqrtn = (int) Math.sqrt(n);

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

    // Helper function to fill the bitmask array with the numbers possible in each cell
    private void preparePossibilitiesArray() {
        possible = new int[n][n];
        // Fill with bitmasks of all 1s, initially assume all numbers are possible (except 0)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                possible[i][j] = (1 << n+1) - 1;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) { // If a cell is already populated
                    // Fill in bitmasks in row/column to not include this number
                    for (int k = 0; k < n; k++) {
                        possible[k][j] &= ~(1 << board[i][j]);
                        possible[i][k] &= ~(1 << board[i][j]);
                    }
                    // Fill in bitmasks in square to not include this number
                    for (int k = (i/sqrtn)*sqrtn; k < ((i/sqrtn) + 1)*sqrtn; k++) {
                        for (int l = (j/sqrtn)*sqrtn; l < ((j/sqrtn) + 1)*sqrtn; l++) {
                            possible[k][l] &= ~(1 << board[i][j]);
                        }
                    }
                }
            }
        }
    }

    // Helper function that makes a copy of the board
    public Board copy() {
        // Make a copy of the board array
        int[][] boardNow = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardNow[i][j] = board[i][j];
            }
        }

        // Make a copy of the possible array
        int[][] possibleNow = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                possibleNow[i][j] = possible[i][j];
            }
        }

        return new Board(n, boardNow, possibleNow);
    }
}
