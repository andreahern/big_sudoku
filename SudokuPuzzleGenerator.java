/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Generates a valid nxn sudoku board.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.*;

public class SudokuPuzzleGenerator {
    private int n, sqrtn;
    private double difficulty;
    private int[][] board;

    public SudokuPuzzleGenerator(int n, double difficulty) {
        this.n = n;
        sqrtn = (int) Math.sqrt(n);
        board = new int[n][n];
        this.difficulty = difficulty;
    }

    // Helper function to generate a valid solved board
    public void generateCompleteBoard() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            options.add(i);
        }

        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int selection = options.get(rand.nextInt(options.size()));
            options.remove(Integer.valueOf(selection));
            board[0][i] = selection;
        }

        int nextOneShift = sqrtn;
        for (int i = 1; i < n; i++) {
            int shift;
            if (i == nextOneShift) {
                nextOneShift += sqrtn;
                shift = 1;
            } else {
                shift = sqrtn;
            }
            board[i] = shiftRow(board[i - 1], shift);
        }
    }

    // Helper function to shift a row, helping in generating a solved board
    private int[] shiftRow(int[] row, int shift) {
        int newRow[] = new int[n];

        for (int i = 0; i < n; i++) {
            newRow[i] = row[shift++ % n];
        }

        return newRow;
    }

    // Helper function to delete a number of cells based on the difficulty, making a puzzle out of a
    // valid completed board
    private void deleteCells() {
        int numToDelete = (int) (difficulty * n * n);

        // Make an arraylist holding the indexes of all the undeleted cells
        ArrayList<Integer> cells = new ArrayList<>();
        for (int i = 0; i < n*n; i++) cells.add(i);

        for (int i = 0; i < numToDelete; i++) {
            // Pick a random cell out of the arraylist to delete
            int rand = (int) (Math.random() * cells.size());
            int cell = cells.get(rand);
            int del_i = cell / n;
            int del_j = cell % n;

            // Delete the corresponding cell on the board
            if (board[del_i][del_j] != 0) board[del_i][del_j] = 0;

            // Remove the value from the arraylist
            cells.remove(rand);
        }
    }

    public int[][] generatePuzzle() {
        generateCompleteBoard();
        deleteCells();
        return board;
    }

    public void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print((board[i][j] >= 10 ? board[i][j] : "0" + board[i][j]) + (j == n-1 ? "\n" : " "));
            }
        }
    }
}
