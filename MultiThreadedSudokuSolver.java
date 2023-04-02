/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Multi-threaded program that finds all solutions to a sudoku board using a forking
// backtracking method.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class MultiThreadedSudokuSolver {
    private static int numThreads = 6;

    // Array list to store solution(s)
    private static SolutionList solutions;

    // List of threads
    private static SudokuThread[] threads;

    // Helper method to prepare the sudoku board and solve it
    public static ArrayList<int[][]> solve(Board board) {
        // Initialize solutions list
        solutions = new SolutionList();

        // Initialize threads
        TaskQueue queue = new TaskQueue();
        threads = new SudokuThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SudokuThread(queue, solutions);
        }

        // Start all threads
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }

        // Put first task in queue
        queue.addTask(new Task(board, 0));

        // Join all threads
        for (int i = 0; i < numThreads; i++) {
            try {
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
