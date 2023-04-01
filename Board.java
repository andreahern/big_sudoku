/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Board object that stores a sudoku board and all possibilities for each empty space
//
/////////////////////////////////////////////////////////////////////////////////////////////////

public class Board {
    public int n, sqrtn;
    public int[][] board, possible;

    // Constructor given only board
    public Board(int[][] board) {
        n = board.length;
        sqrtn = (int) Math.sqrt(n);
        this.board = board;
        preparePossibilitiesArray();
    }

    // Constructor given board and possible
    private Board(int[][] board, int[][] possible) {
        n = board.length;
        sqrtn = (int) Math.sqrt(n);
        this.board = board;
        this.possible = possible;
    }

    // Helper function to fill the bitmask array with the numbers possible in each cell
    private void preparePossibilitiesArray() {
        possible = new int[n][n];
        // Fill with bitmasks of all 1s, initially assume all numbers are possible (except 0)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                possible[i][j] = (1 << n+1) - 1;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) { // If a cell is already populated
                    // Fill in bitmasks in row/column to not include this number
                    for (int k = 0; k < n; k++) {
                        possible[k][j] &= ~(1 << board[i][j]);
                        possible[i][k] &= ~(1 << board[i][j]);
                    }
                    // Fill in bitmasks in square to not include this number
                    for (int k = (i/sqrtn)*sqrtn; k < ((i/sqrtn) + 1)*sqrtn; k++) {
                        for (int l = (j/sqrtn)*sqrtn; l < ((j/sqrtn) + 1)*sqrtn; l++) {
                            possible[k][l] &= ~(1 << board[i][j]);
                        }
                    }
                }
            }
        }
    }

    // Helper function that makes a copy of the board
    public Board copy() {
        // Make a copy of the board array
        int[][] boardNow = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardNow[i][j] = board[i][j];
            }
        }

        // Make a copy of the possible array
        int[][] possibleNow = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                possibleNow[i][j] = possible[i][j];
            }
        }

        return new Board(boardNow, possibleNow);
    }
}
