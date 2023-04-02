/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Multi-threaded program that finds all solutions to a sudoku board using a task queue.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class MultiThreadedBFS {
    private static int numThreads = 6;

    // Array list to store solution(s)
    private static SolutionList solutions;

    // List of threads
    private static SudokuThreadBFS[] threads;

    // Main method to call to solve the board
    public static ArrayList<int[][]> solve(Board board) {
        // Initialize solutions list
        solutions = new SolutionList();

        // Initialize threads
        TaskQueue queue = new TaskQueue();
        threads = new SudokuThreadBFS[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SudokuThreadBFS(queue, solutions);
        }

        queue.startWorking();
        // Start all threads
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }

        // Put first task in queue
        queue.addTask(new Task(board, 0));
        queue.stopWorking();

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
