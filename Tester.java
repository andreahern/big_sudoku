/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Allows us to test our program
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Tester {
    public static void main(String[] args) {
        // args[0]: number of boards to generate
        // args[1]: size length of the boards
        int numBoards = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        // Create a new board generator
        CompleteBoardGenerator generator = new CompleteBoardGenerator(n);

        // Keep track of times for each approach
        long singleThreadedTime = 0;
        long multiThreadedTime = 0;

        System.out.println("\tSingle-threaded \tMulti-threaded");
        // Generate and solve numBoards boards
        for (int i = 0; i < numBoards; i++) {
            // Generate a new complete valid board
            generator.generateBoard();
            int[][] newBoard = generator.getBoard();

            // Randomly clear some of the cells to create a valid sudoku puzzle
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (Math.random() < 0.33) newBoard[j][k] = 0;
                }
            }

            // Solve the board
            long[] ans = SudokuSolver.solve(newBoard);

            // Update total time taken
            singleThreadedTime += ans[0];
            multiThreadedTime += ans[1];

            System.out.println("\t" + ans[0] + " ms\t\t\t" + ans[1] + " ms");
        }

        // Find average time taken for each approach
        double singleThreadedAvg = (double)singleThreadedTime/numBoards;
        double multiThreadedAvg = (double)multiThreadedTime/numBoards;

        System.out.println("\t----------------------------------------");
        System.out.println("avg\t" + singleThreadedAvg + "ms\t\t\t" + multiThreadedAvg + " ms");
        System.out.println("avg improvement: " + (multiThreadedAvg - singleThreadedAvg) + "ms\n");
    }
}
