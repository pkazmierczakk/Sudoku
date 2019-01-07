package Controllers;
import Exceptions.UnableToRedoMoveException;
import Exceptions.UnableToUndoMoveException;
import Models.Board;
import Models.Field;
import Models.Region;
import Models.Coordinate;
import Models.PlayerMove;
import Views.FriendlyRepresentField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class SudokuController implements Serializable {
    public SudokuController() {
        generateBoard();
    }

    private Board board = new Board();
    private ArrayList<PlayerMove> movesOfPlayer = new ArrayList<>();
    private int currentMoveIndex = 0;

    private ArrayList<Coordinate> checkRegions() {
        Region []regions = getRegions();
        Field [][] board = getRawBoard();
        ArrayList<Coordinate> wrongFieldsCoords = new ArrayList<>();
        for (int regionIndex = 0; regionIndex < 9; regionIndex++) {
            int []usedNumbers = new int[10];
            Coordinate[] regionCoords = regions[regionIndex].getCoords();
            for (int coordIndex = 0; coordIndex < 9; coordIndex++) {
                int coordX1 = regionCoords[coordIndex].getCoordX();
                int coordY1 = regionCoords[coordIndex].getCoordY();

                int coordX2 = regionCoords[8-coordIndex].getCoordX();
                int coordY2 = regionCoords[8-coordIndex].getCoordY();

                usedNumbers[board[coordY1][coordX1].getValue()] += 1;
                usedNumbers[board[coordY2][coordX2].getValue()] += 1;

                if (board[coordY1][coordX1].getValue() > 0 && usedNumbers[board[coordY1][coordX1].getValue()] > 2) {
                    wrongFieldsCoords.add(regionCoords[coordIndex]);
                }

                if (board[coordY2][coordX2].getValue() > 0 && usedNumbers[board[coordY2][coordX2].getValue()] > 2) {
                    wrongFieldsCoords.add(regionCoords[8-coordIndex]);
                }
            }
        }
        return wrongFieldsCoords;
    }

    private ArrayList<Coordinate> checkHorizontallyAndVertically() {
        Field[][] rawBoard = getRawBoard();
        ArrayList<Coordinate> wrongFieldsCoords = new ArrayList<>();

        for (int coord1 = 0; coord1 < 9; coord1++) {
            int []usedNumbersHorizontally = new int[10];
            int []usedNumbersVertically = new int[10];
            for (int coord2 = 0; coord2 < 9; coord2++) {
                usedNumbersHorizontally[rawBoard[coord1][coord2].getValue()]++;
                usedNumbersHorizontally[rawBoard[coord1][8-coord2].getValue()]++;
                usedNumbersVertically[rawBoard[coord2][coord1].getValue()]++;
                usedNumbersVertically[rawBoard[8-coord2][coord1].getValue()]++;

                if (rawBoard[coord1][coord2].getValue() > 0 && usedNumbersHorizontally[rawBoard[coord1][coord2].getValue()] > 2) {
                    wrongFieldsCoords.add(new Coordinate(coord2, coord1));
                }
                if (rawBoard[coord1][8-coord2].getValue() > 0 && usedNumbersHorizontally[rawBoard[coord1][8-coord2].getValue()] > 2) {
                    wrongFieldsCoords.add(new Coordinate(8-coord2, coord1));
                }
                if (rawBoard[coord2][coord1].getValue() > 0 && usedNumbersVertically[rawBoard[coord2][coord1].getValue()] > 2) {
                    wrongFieldsCoords.add(new Coordinate(coord1, coord2));
                }
                if (rawBoard[8-coord2][coord1].getValue() > 0 && usedNumbersVertically[rawBoard[8-coord2][coord1].getValue()] > 2) {
                    wrongFieldsCoords.add(new Coordinate(coord1, 8-coord2));
                }
            }
        }
        return wrongFieldsCoords;
    }

    public ArrayList<Coordinate> getEmptyFields() {
        Field[][] rawBoard = getRawBoard();
        ArrayList<Coordinate> wrongFieldsCoords = new ArrayList<>();
        for (int coordY = 0; coordY < 9; coordY++) {
            for (int coordX = 0; coordX < 9; coordX++) {
                if (rawBoard[coordY][coordX].getValue() == 0) {
                    wrongFieldsCoords.add(new Coordinate(coordX, coordY));
                }
            }
        }
        return wrongFieldsCoords;
    }

    public void setBoard(Field[][]board) {
        this.board.setBoard(board);
    }

    public Field[][] getRawBoard() {
        return this.board.getBoard();
    }

    public void setField(Coordinate coord, int val, boolean saveHistory) {
        if (saveHistory) {
            movesOfPlayer = new ArrayList<>(movesOfPlayer.subList(0, this.currentMoveIndex));
            this.currentMoveIndex++;
//            movesOfPlayer.add(new PlayerMove(coord, getFieldValue(coord)));
            if (getFieldValue(coord) == 0) {
                movesOfPlayer.add(new PlayerMove(coord, 0));
                this.currentMoveIndex++;
            }
            movesOfPlayer.add(new PlayerMove(coord, val));
        }

        board.setFieldVal(coord.getCoordX(), coord.getCoordY(), val);
    }

    public int getFieldValue(Coordinate coords) {
        return this.board.getFieldValue(coords.getCoordX(), coords.getCoordY()); // TODO Change structure of it
    }

    public Region[] getRegions() {
        return board.getRegions();
    }

    public void setRegions(Region[] regions) {
        this.board.setRegions(regions);
    }

    public ArrayList<Coordinate> checkSolution() {
        ArrayList<Coordinate> wrongFieldsCoords = checkRegions();
        wrongFieldsCoords.addAll(checkHorizontallyAndVertically());
        return wrongFieldsCoords;
    }

    public PlayerMove getUndoMove() throws UnableToUndoMoveException {
//        for (PlayerMove p : movesOfPlayer) {
//            System.out.print(p.getPrevValue() + " ");
//        }
//        System.out.println();
        if (this.currentMoveIndex < 1) {
            throw new UnableToUndoMoveException();
        }
        this.currentMoveIndex--;
        PlayerMove currentMove = this.movesOfPlayer.get(currentMoveIndex);
        if (currentMove.getPrevValue() == getFieldValue(currentMove.getCoords())) {
            this.currentMoveIndex--;
            currentMove = this.movesOfPlayer.get(currentMoveIndex);
        }
        setField(currentMove.getCoords(), currentMove.getPrevValue(), false);
        return currentMove;
    }

    public PlayerMove getRedoMove() throws UnableToRedoMoveException {
        if (this.currentMoveIndex >= movesOfPlayer.size()-1) {
            throw new UnableToRedoMoveException();
        }
        this.currentMoveIndex++;
        PlayerMove currentMove = this.movesOfPlayer.get(currentMoveIndex);
        if (currentMove.getPrevValue() == getFieldValue(currentMove.getCoords())) {
            this.currentMoveIndex++;
            currentMove = this.movesOfPlayer.get(currentMoveIndex);
        }
        setField(currentMove.getCoords(), currentMove.getPrevValue(), false);
        return currentMove;
    }

    private void generateBoard() {
        int amountOfNumbers = 27;
        Field[][] board = new Field[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Field();
            }
        }
        setBoard(board);

//        Field[][]board = {
//                {new Field(0), new Field(2), new Field(0), new Field(9), new Field(0), new Field(0), new Field(0), new Field(5), new Field(0)},
//                {new Field(5), new Field(0), new Field(0), new Field(0), new Field(0), new Field(4), new Field(0), new Field(0), new Field(8)},
//                {new Field(0), new Field(0), new Field(6), new Field(0), new Field(0), new Field(0), new Field(0), new Field(0), new Field(0)},
//
//                {new Field(0), new Field(5), new Field(0), new Field(0), new Field(3), new Field(0), new Field(7), new Field(0), new Field(0)},
//                {new Field(9), new Field(0), new Field(0), new Field(1), new Field(0), new Field(6), new Field(0), new Field(0), new Field(3)},
//                {new Field(0), new Field(0), new Field(2), new Field(0), new Field(8), new Field(0), new Field(0), new Field(4), new Field(0)},
//
//                {new Field(0), new Field(0), new Field(0), new Field(0), new Field(0), new Field(0), new Field(4), new Field(0), new Field(0)},
//                {new Field(3), new Field(0), new Field(0), new Field(7), new Field(0), new Field(0), new Field(0), new Field(0), new Field(9)},
//                {new Field(0), new Field(7), new Field(0), new Field(0), new Field(0), new Field(8), new Field(0), new Field(2), new Field(0)},
//        };

//        Field[][]board = {
//                //new Field(5),new Field(3),new Field(4)
//                {new Field(0),new Field(0),new Field(0),new Field(6),new Field(7),new Field(8),new Field(9),new Field(1),new Field(2)},
//                {new Field(6),new Field(7),new Field(2),new Field(1),new Field(9),new Field(5),new Field(3),new Field(4),new Field(8)},
//                {new Field(1),new Field(9),new Field(8),new Field(3),new Field(4),new Field(2),new Field(5),new Field(6),new Field(7)},
//
//                {new Field(8),new Field(5),new Field(9),new Field(7),new Field(6),new Field(1),new Field(4),new Field(2),new Field(3)},
//                {new Field(4),new Field(2),new Field(6),new Field(8),new Field(5),new Field(3),new Field(7),new Field(9),new Field(1)},
//                {new Field(7),new Field(1),new Field(3),new Field(9),new Field(2),new Field(4),new Field(8),new Field(5),new Field(6)},
//
//                {new Field(9),new Field(6),new Field(1),new Field(5),new Field(3),new Field(7),new Field(2),new Field(8),new Field(4)},
//                {new Field(2),new Field(8),new Field(7),new Field(4),new Field(1),new Field(9),new Field(6),new Field(3),new Field(5)},
//                {new Field(3),new Field(4),new Field(5),new Field(2),new Field(8),new Field(6),new Field(1),new Field(7),new Field(9)},
//        };
//        setBoard(board);

        Region[] regions = new Region[] {
                new Region(0, new Coordinate[] {new Coordinate(0,0), new Coordinate(1,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(2,1), new Coordinate(0,2), new Coordinate(2,2), new Coordinate(3,2), new Coordinate(2,3)}),
                new Region(1, new Coordinate[] {new Coordinate(2,0), new Coordinate(3,0), new Coordinate(4,0), new Coordinate(5,0), new Coordinate(6,0), new Coordinate(3,1), new Coordinate(4,1), new Coordinate(5,1), new Coordinate(4,2)}),
                new Region(2, new Coordinate[] {new Coordinate(7,0), new Coordinate(8,0), new Coordinate(6,1), new Coordinate(7,1), new Coordinate(8,1), new Coordinate(5,2), new Coordinate(6,2), new Coordinate(8,2), new Coordinate(5,3)}),
                new Region(3, new Coordinate[] {new Coordinate(1,2), new Coordinate(0,3), new Coordinate(1,3), new Coordinate(0,4), new Coordinate(1,4), new Coordinate(2,4), new Coordinate(0,5), new Coordinate(1,5), new Coordinate(0,6)}),
                new Region(4, new Coordinate[] {new Coordinate(3,3), new Coordinate(4,3), new Coordinate(3,4), new Coordinate(4,4), new Coordinate(5,4), new Coordinate(4,5), new Coordinate(5,5), new Coordinate(6,5), new Coordinate(5,6)}),
                new Region(5, new Coordinate[] {new Coordinate(7,2), new Coordinate(6,3), new Coordinate(7,3), new Coordinate(8,3), new Coordinate(6,4), new Coordinate(7,4), new Coordinate(7,5), new Coordinate(7,6), new Coordinate(7,7)}),
                new Region(6, new Coordinate[] {new Coordinate(2,5), new Coordinate(3,5), new Coordinate(1,6), new Coordinate(2,6), new Coordinate(0,7), new Coordinate(1,7), new Coordinate(0,8), new Coordinate(1,8), new Coordinate(2,8)}),
                new Region(7, new Coordinate[] {new Coordinate(3,6), new Coordinate(4,6), new Coordinate(2,7), new Coordinate(3,7), new Coordinate(4,7), new Coordinate(5,7), new Coordinate(3,8), new Coordinate(4,8), new Coordinate(5,8)}),
                new Region(8, new Coordinate[] {new Coordinate(8,4), new Coordinate(8,5), new Coordinate(6,6), new Coordinate(8,6), new Coordinate(6,7), new Coordinate(8,7), new Coordinate(6,8), new Coordinate(7,8), new Coordinate(8,8)}),
        };

//        Region[] regions = new Region[] {
//                new Region(0, new Coordinate[] {new Coordinate(0,0), new Coordinate(1,0), new Coordinate(2,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(2,1), new Coordinate(0,2), new Coordinate(1,2), new Coordinate(2,2)}),
//                new Region(1, new Coordinate[] {new Coordinate(3,0), new Coordinate(4,0), new Coordinate(5,0), new Coordinate(3,1), new Coordinate(4,1), new Coordinate(5,1), new Coordinate(3,2), new Coordinate(4,2), new Coordinate(5,2)}),
//                new Region(2, new Coordinate[] {new Coordinate(6,0), new Coordinate(7,0), new Coordinate(8,0), new Coordinate(6,1), new Coordinate(7,1), new Coordinate(8,1), new Coordinate(6,2), new Coordinate(7,2), new Coordinate(8,2)}),
//
//                new Region(3, new Coordinate[] {new Coordinate(0,3), new Coordinate(1,3), new Coordinate(2,3), new Coordinate(0,4), new Coordinate(1,4), new Coordinate(2,4), new Coordinate(0,5), new Coordinate(1,5), new Coordinate(2,5)}),
//                new Region(4, new Coordinate[] {new Coordinate(3,3), new Coordinate(4,3), new Coordinate(5,3), new Coordinate(3,4), new Coordinate(4,4), new Coordinate(5,4), new Coordinate(3,5), new Coordinate(4,5), new Coordinate(5,5)}),
//                new Region(5, new Coordinate[] {new Coordinate(6,3), new Coordinate(7,3), new Coordinate(8,3), new Coordinate(6,4), new Coordinate(7,4), new Coordinate(8,4), new Coordinate(6,5), new Coordinate(7,5), new Coordinate(8,5)}),
//
//                new Region(6, new Coordinate[] {new Coordinate(0,6), new Coordinate(1,6), new Coordinate(2,6), new Coordinate(0,7), new Coordinate(1,7), new Coordinate(2,7), new Coordinate(0,8), new Coordinate(1,8), new Coordinate(2,8)}),
//                new Region(7, new Coordinate[] {new Coordinate(3,6), new Coordinate(4,6), new Coordinate(5,6), new Coordinate(3,7), new Coordinate(4,7), new Coordinate(5,7), new Coordinate(3,8), new Coordinate(4,8), new Coordinate(5,8)}),
//                new Region(8, new Coordinate[] {new Coordinate(6,6), new Coordinate(7,6), new Coordinate(8,6), new Coordinate(6,7), new Coordinate(7,7), new Coordinate(8,7), new Coordinate(6,8), new Coordinate(7,8), new Coordinate(8,8)}),
//        };

        setRegions(regions);

        Random random = new Random();
        ArrayList<Coordinate> coords = new ArrayList<>(amountOfNumbers);
        // Rand coordinates
        for (int i = 0; i < amountOfNumbers; i++) {
            int x = random.nextInt(9);
            int y =  random.nextInt(9);
            Coordinate newCoord = new Coordinate(x,y);
            if (coords.indexOf(newCoord) == -1) {
                coords.add(newCoord);
            } else {
                i -= 1;
            }
        }
        // set randed coordinates as uneditable fields by player
        for (Coordinate coord : coords) {
            board[coord.getCoordY()][coord.getCoordX()].setEditable(false);
        }

        // Set random values until all are correct
        while(!coords.isEmpty()) {
            for (Coordinate coord : coords) {
                this.board.setFieldVal(coord.getCoordX(), coord.getCoordY(), random.nextInt(9)+1);
            }
            coords = checkSolution();
        }
    }

    public FriendlyRepresentField[][] getBoard() {
        FriendlyRepresentField [][] friendlyBoard = new FriendlyRepresentField[9][9];
        Region []regions = getRegions();
        Field [][] rawBoard = getRawBoard();
        for (int regionIndex = 0; regionIndex < 9; regionIndex++) {
            Coordinate [] regionCoords = regions[regionIndex].getCoords();
            int idRegion = regions[regionIndex].getIdRegion();

            for (int coordIndex = 0; coordIndex < 9; coordIndex++) {
                int coordX = regionCoords[coordIndex].getCoordX(), coordY = regionCoords[coordIndex].getCoordY();
                friendlyBoard[coordY][coordX] = new FriendlyRepresentField(idRegion, rawBoard[coordY][coordX].getValue(), new Coordinate(coordX, coordY), rawBoard[coordY][coordX].isEditable());
            }
        }
        return friendlyBoard;
    }
}
