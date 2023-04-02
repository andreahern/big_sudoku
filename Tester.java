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
        SudokuPuzzleGenerator generator = new SudokuPuzzleGenerator(n, difficulty);

        // Keep track of times for each approach
        long singleThreadedDFSTime = 0;
        long singleThreadedBFSTime = 0;
        long multiThreadedDFSTime = 0;
        long multiThreadedBFSTime = 0;

        System.out.printf("\n\t%-25s %-25s %-25s %-25s\n\n", "Single-threaded DFS", "Single-threaded BFS", "Multi-threaded DFS", "Multi-threaded BFS");
        // Generate and solve numBoards boards
        for (int i = 0; i < numBoards; i++) {
            // Generate a new puzzle
            int[][] newBoard = generator.generatePuzzle();

            // Solve the board
            long[] ans = SudokuSolver.solve(newBoard);

            // Update total time taken
            singleThreadedDFSTime += ans[0];
            singleThreadedBFSTime += ans[1];
            multiThreadedDFSTime += ans[2];
            multiThreadedBFSTime += ans[2];

            System.out.printf("\t%-25s %-25s %-25s %-25s\n", ans[0] + " ms", ans[1] + " ms", ans[2] + " ms", ans[3] + " ms");
        }

        // Find average time taken for each approach
        double singleThreadedDFSAvg = (double)singleThreadedDFSTime/numBoards;
        double singleThreadedBFSAvg = (double)singleThreadedBFSTime/numBoards;
        double multiThreadedDFSAvg = (double)multiThreadedDFSTime/numBoards;
        double multiThreadedBFSAvg = (double)multiThreadedBFSTime/numBoards;

        System.out.println("\t----------------------------------------------------------------------------------------------------");
        System.out.printf("avg\t%-25s %-25s %-25s %-25s\n\n", singleThreadedDFSAvg + " ms", singleThreadedBFSAvg + " ms", multiThreadedDFSAvg + " ms", multiThreadedBFSAvg + " ms");
        System.out.printf("BFS avg improvement: %5f ms\n", singleThreadedBFSAvg - multiThreadedBFSAvg);
        System.out.printf("DFS avg improvement: %5f ms\n\n", singleThreadedDFSAvg - multiThreadedDFSAvg);
    }
}
