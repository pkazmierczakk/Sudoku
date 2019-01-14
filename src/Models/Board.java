package Models;

import java.io.Serializable;

public class Board  implements Serializable {
    private Field [][] board;
    private Region [] regions;

    public Board() {
    }

    public Field [][] getBoard() {
        return board;
    }

    public void setBoard(Field [][] board) {
        this.board = board;
    }

    public void setFieldVal(int coordX, int coordY, int val) {
        board[coordY][coordX].setValue(val);
    }

    public void setFieldVal(int coordX, int coordY, int val, boolean isEditable) {
        board[coordY][coordX].setValue(val);
        board[coordY][coordX].setEditable(isEditable);
    }

    public int getFieldValue(int coordX, int coordY) { return board[coordY][coordX].getValue(); }

    public Region[] getRegions() {
        return regions;
    }

    public void setRegions(Region[] regions) {
        this.regions = regions;
    }
}
