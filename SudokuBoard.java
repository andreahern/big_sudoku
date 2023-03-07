import java.util.ArrayList;
import java.util.Random;

public class SudokuBoard {
    private int board[][];
    private int completed_board[][];

    public SudokuBoard() {
        board = new int[25][25];
    }

    public void generate_board() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            options.add(i);
        }

        Random rand = new Random();
        for (int i = 0; i < 25; i++) {
            int selection = options.get(rand.nextInt(options.size()));
            options.remove(Integer.valueOf(selection));
            board[0][i] = selection;
        }

        int next_one_shift = 5;
        for (int i = 1; i < 25; i++) {
            int shift;
            if (i == next_one_shift) {
                next_one_shift += 5;
                shift = 1;
            } else {
                shift = 5;
            }
            board[i] = shift_row(board[i - 1], shift);
        }

        completed_board = board.clone();
    }

    private int[] shift_row(int[] row, int n) {
        int new_row[] = new int[25];

        for (int i = 0; i < 25; i++) {
            new_row[i] = row[n++ % 25];
        }

        return new_row;
    }

    public int[][] get_board() {
        return board;
    }

    public void print_board() {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print((board[i][j] >= 10 ? board[i][j] : "0" + board[i][j]) + (j == 24 ? "\n" : " "));
            }
        }
    }
}
