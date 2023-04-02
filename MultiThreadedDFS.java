/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Multi-threaded program that finds all solutions to a sudoku board using a recursive
// backtracking method.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class MultiThreadedDFS {
    private static int numThreads = 6;

    // Array list to store solution(s)
    private static SolutionList solutions;

    // Main method to call to solve the board
    public static ArrayList<int[][]> solve(Board board) {
        // Initialize solutions list
        solutions = new SolutionList();

        // Initialize list of threads
        SudokuThreadDFS[] threads = new SudokuThreadDFS[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SudokuThreadDFS(solutions);
        }

        // Find the first empty cell in the board
        int start_i = 0, start_j = 0;
        while (board.board[start_i][start_j] != 0) {
            start_j++;
            if (start_j >= board.n) {
                start_j = 0;
                start_i++;
                if (start_i >= board.n) return solutions.getSolutions();
            }
        }

        // Distribute possible starting moves among the threads
        int thread = 0;
        for (int i = 1; i < board.n + 1; i++) {
            if ((board.possible[start_i][start_j] & (1 << i)) == (1 << i)) { // If the number is possible in the first cell
                // Place it in the cell
                Board newBoard = board.tryPlacement(i, start_i, start_j);
                // Give the new task to the current thread
                threads[thread].giveNewTask(new Task(newBoard, start_j + (start_i * board.n)));
                // Increment thread number
                thread++;
                thread %= numThreads;
            }
        }

        // Start and join all threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].start();
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("The thread was interrupted");
                e.printStackTrace();
            }
        }

        return solutions.getSolutions();
    }
}
