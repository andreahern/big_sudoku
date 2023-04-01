/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Generates a valid completed nxn sudoku board.
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Random;

public class CompleteBoardGenerator {
    private int n, sqrtn;
    private int board[][];
    private int completed_board[][];

    public SudokuBoard(int n) {
        this.n = n;
        if ((int) Math.sqrt(n) != Math.sqrt(n)) return null;
        sqrtn = (int) Math.sqrt(n);
        board = new int[n][n];
    }

    public void generate_board() {
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
            board[i] = shift_row(board[i - 1], shift);
        }

        completed_board = board.clone();
    }

    private int[] shift_row(int[] row, int n) {
        int new_row[] = new int[n];

        for (int i = 0; i < n; i++) {
            new_row[i] = row[n++ % n];
        }

        return new_row;
    }

    public int[][] get_board() {
        return board;
    }

    public void print_board() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print((board[i][j] >= 10 ? board[i][j] : "0" + board[i][j]) + (j == n-1 ? "\n" : " "));
            }
        }
    }
}
