/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Multi-threaded program that finds all solutions to a sudoku board using a forking
// backtracking method.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class MultiThreadedSudokuSolver {
    // Array to store solution(s)
    private static ArrayList<int[][]> solutions;

    // Helper method to prepare the sudoku board and solve it
    public static ArrayList<int[][]> solve(Board board) {
        // Initialize solutions ArrayList
        solutions = new ArrayList<>();

        // Solve the board
        recursiveSolve(board, 0);

        return solutions;
    }
