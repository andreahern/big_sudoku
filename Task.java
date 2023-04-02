/////////////////////////////////////////////////////////////////////////////////////////////////
//
// Task class that represents one step of recursion
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

public class Task {
    private Board board;
    private int cell;

    public Task(Board board, int cell) {
        this.board = board;
        this.cell = cell;
    }

    public Board getBoard() {
        return board;
    }

    public int getCell() {
        return cell;
    }
}
