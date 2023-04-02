/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Single-threaded program that finds all solutions to a sudoku board using a queue.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SingleThreadedBFS {
    // Main method to call to solve the board
    public static ArrayList<int[][]> solve(Board board) {
        // Array list to store solution(s)
        ArrayList<int[][]> solutions = new ArrayList<>();
        // Queue to hold tasks
        ArrayDeque<Task> tasks = new ArrayDeque<>();

        // Add first state to queue
        tasks.add(new Task(board, 0));

        while (!tasks.isEmpty()) {
            // Get the next task to do
            Task curTask = tasks.poll();

            Board curBoard = curTask.getBoard();
            int cell = curTask.getCell();
            // Extrapolate current i and j values from current cell number (0-indexed)
            // e.g. for a 3x3 board, cell numbers are:
            //      0 1 2
            //      3 4 5
            //      6 7 8
            int at_i = cell / board.n;
            int at_j = cell % board.n;

            // If it's out of bounds, the board is solved so put the board in the solutions ArrayList and continue
            if (at_i >= board.n || at_j >= board.n) {
                solutions.add(board.board);
                continue;
            }

            // If nothing's possible in this cell, no solution possible from this point so continue
            if (board.possible[at_i][at_j] == 0) continue;

            // If there's already something in this cell, just go to the next cell
            if (board.board[at_i][at_j] != 0) {
                tasks.add(new Task(board, cell + 1));
                continue;
            }

            // Go through each possible number to go in this cell
            for (int i = 1; i < board.n + 1; i++) {
                if ((board.possible[at_i][at_j] & (1 << i)) == (1 << i)) { // If the number is possible in this cell
                    // Place it in the cell
                    Board newBoard = board.tryPlacement(i, at_i, at_j);

                    // Add this new state to the tasks queue
                    tasks.add(new Task(newBoard, cell + 1));
                }
            }
        }

        return solutions;
    }
}
