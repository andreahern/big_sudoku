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
        // args[2]: difficulty (between 0.0 and 1.0)
        int numBoards = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double difficulty = Double.parseDouble(args[2]);

        // Create a new board generator
        CompleteBoardGenerator generator = new CompleteBoardGenerator(n);

        // Keep track of times for each approach
        long singleThreadedDFSTime = 0;
        long singleThreadedBFSTime = 0;
        long multiThreadedBFSTime = 0;

        System.out.println("\tSingle-threaded DFS\tSingle-threaded BFS\tMulti-threaded BFS");
        // Generate and solve numBoards boards
        for (int i = 0; i < numBoards; i++) {
            // Generate a new complete valid board
            generator.generateBoard();
            int[][] newBoard = generator.getBoard();

            // Randomly clear some of the cells to create a valid sudoku puzzle
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (Math.random() < difficulty) newBoard[j][k] = 0;
                }
            }

            // Solve the board
            long[] ans = SudokuSolver.solve(newBoard);

            // Update total time taken
            singleThreadedDFSTime += ans[0];
            singleThreadedBFSTime += ans[1];
            multiThreadedBFSTime += ans[2];

            System.out.println("\t" + ans[0] + " ms\t\t\t" + ans[1] + " ms\t\t\t" + ans[2] + " ms");
        }

        // Find average time taken for each approach
        double singleThreadedDFSAvg = (double)singleThreadedDFSTime/numBoards;
        double singleThreadedBFSAvg = (double)singleThreadedBFSTime/numBoards;
        double multiThreadedBFSAvg = (double)multiThreadedBFSTime/numBoards;

        System.out.println("\t----------------------------------------");
        System.out.println("avg\t" + singleThreadedDFSAvg + "ms\t\t\t" + singleThreadedBFSAvg + " ms\t\t\t" + multiThreadedBFSAvg + " ms");
        System.out.println("BFS avg improvement: " + (singleThreadedBFSAvg - multiThreadedBFSAvg) + "ms\n");
    }
}
