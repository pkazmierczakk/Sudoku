package Exceptions;

public class SudokuException extends Exception {
    private String msg;

    public SudokuException(String msg) {
        this.msg = msg;
    }
    public SudokuException() {
        this.msg = "";
    }

    public String toString() {
        return msg;
    }
}
