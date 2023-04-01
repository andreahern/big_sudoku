/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Checks if the given solution is a valid solution for the given sudoku puzzle
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SolutionChecker {
    public static boolean check(int[][] original, int[][] solution) {
        int n = original.length;
        int sqrtn = (int) Math.sqrt(n);

        // Check that none of the initial numbers have been changed
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (original[i][j] != 0) {
                    // If a number from the original puzzle has been changed, the solution is invalid
                    if (solution[i][j] != original[i][j]) return false;
                }
            }
        }

        // Check if each row has exactly one instance of each number 1 to n
        for (int i = 0; i < n; i++) {
            // Create bitmap to keep tally of each number seen
            long seen = 0;
            for (int j = 0; j < n; j++) {
                // Add each number seen to bitmap
                seen += (1 << solution[i][j]);
            }
            // If each number is not seen, the solution is invalid
            if (seen != (1 << n + 1) - 2) return false;
        }

        // Check if each column has exactly one instance of each number 1 to n
        for (int i = 0; i < n; i++) {
            // Create bitmap to keep tally of each number seen
            long seen = 0;
            for (int j = 0; j < n; j++) {
                // Add each number seen to bitmap
                seen += (1 << solution[j][i]);
            }
            // If each number is not seen, the solution is invalid
            if (seen != (1 << n + 1) - 2) return false;
        }

        // Check if each square has exactly one instance of each number 1 to n
        for (int i = 0; i < sqrtn; i+= sqrtn) {
            for (int j = 0; j < sqrtn; j+=sqrtn) {
                // Create bitmap to keep tally of each number seen
                long seen = 0;
                for (int k = 0; k < sqrtn; k++) {
                    for (int l = 0; l < sqrtn; l++) {
                        seen += (1 << solution[i + k][j + l]);
                    }
                }
                // If each number is not seen, the solution is invalid
                if (seen != (1 << n + 1) - 2) return false;
            }
        }

        // If it got through all the checks, the solution is valid
        return true;
    }
}
