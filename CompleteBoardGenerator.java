/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Generates a valid completed nxn sudoku board.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.*;

public class CompleteBoardGenerator {
    private int n, sqrtn;
    private int board[][];
    private int completed_board[][];

    public CompleteBoardGenerator(int n) {
        this.n = n;
        sqrtn = (int) Math.sqrt(n);
        board = new int[n][n];
        generateBoard();
    }

    public void generateBoard() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            options.add(i);
        }

        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int selection = options.get(rand.nextInt(options.size()));
            options.remove(Integer.valueOf(selection));
            board[0][i] = selection;
        }

        int next_one_shift = sqrtn;
        for (int i = 1; i < n; i++) {
            int shift;
            if (i == next_one_shift) {
                next_one_shift += sqrtn;
                shift = 1;
            } else {
                shift = sqrtn;
            }
            board[i] = shiftRow(board[i - 1], shift);
        }

        completed_board = board.clone();
    }

    private int[] shiftRow(int[] row, int shift) {
        int new_row[] = new int[n];

        for (int i = 0; i < n; i++) {
            new_row[i] = row[shift++ % n];
        }

        return new_row;
    }

    public int[][] getBoard() {
        return board;
    }

    public void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print((board[i][j] >= 10 ? board[i][j] : "0" + board[i][j]) + (j == n-1 ? "\n" : " "));
            }
        }
    }
}
