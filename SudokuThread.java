public class SudokuThread extends Thread {
    private TaskQueue queue;
    private SolutionList solutions;
    private Task curTask;

    // Constructor
    public SudokuThread(TaskQueue queue, SolutionList solutions) {
        this.queue = queue;
        this.solutions = solutions;
        curTask = null;
    }

    @Override
    public void run() {
        waitForTask();
    }

    private void waitForTask() {
        System.out.println("waitForTask()");
        while (curTask == null) {
            System.out.println("((start of loop))");
            // Try to get new task, if failed go back to sleep
            curTask = queue.getTask();
            if (curTask == null) return;
            doTask();
        }
    }

    private void doTask() {
        System.out.println("doTask()");
        queue.startWorking();
        System.out.println("started working, " + queue.getNumThreadsWorking());
        Board board = curTask.getBoard();
        int cell = curTask.getCell();

        // Extrapolate current i and j values from current cell number (0-indexed)
        // e.g. for a 3x3 board, cell numbers are:
        //      0 1 2
        //      3 4 5
        //      6 7 8
        int at_i = cell / board.n;
        int at_j = cell % board.n;

        // If it's out of bounds, the board is solved so put the board in the solutions ArrayList and return
        if (at_i >= board.n || at_j >= board.n) {
            solutions.addSolution(board.board);
            curTask = null;
            queue.stopWorking();
            System.out.println("stopped working, " + queue.getNumThreadsWorking());
            return;
        }

        // If nothing's possible in this cell, no solution possible from this point so return
        if (board.possible[at_i][at_j] == 0) {
            curTask = null;
            queue.stopWorking();
            System.out.println("stopped working, " + queue.getNumThreadsWorking());
            return;
        }
        // If there's already something in this cell, just go to the next cell
        if (board.board[at_i][at_j] != 0) {
            queue.addTask(new Task(board, cell + 1));
            curTask = null;
            queue.stopWorking();
            System.out.println("stopped working, " + queue.getNumThreadsWorking());
            return;
        }

        // Go through each possible number to go in this cell
        for (int i = 1; i < board.n + 1; i++) {
            if ((board.possible[at_i][at_j] & (1 << i)) == (1 << i)) { // If the number is possible in this cell
                // Place it in the cell
                Board newBoard = board.tryPlacement(i, at_i, at_j);

                // Recurse from here
                queue.addTask(new Task(newBoard, cell + 1));
                curTask = null;
                queue.stopWorking();
                System.out.println("stopped working, " + queue.getNumThreadsWorking());
                return;
            }
        }

        curTask = null;
        queue.stopWorking();
        System.out.println("stopped working, " + queue.getNumThreadsWorking());
        return;
    }
}
