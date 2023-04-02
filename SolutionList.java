/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Solution list class that handles multiple threads coming up with solutions at the same time
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class SolutionList {
    private ArrayList<int[][]> solutions;

    public SolutionList() {
        solutions = new ArrayList<int[][]>();    
    }

    public synchronized void addSolution(int[][] solution) {
        solutions.add(solution);
    }

    public ArrayList<int[][]> getSolutions() {
        return solutions;
    }
}
