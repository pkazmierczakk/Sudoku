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
import java.util.*;


public class SudokuController implements Serializable {
    public SudokuController() {
        generateBoard();
    }

    private Board playerBoard = new Board();
    private Board solvedBoard = new Board();
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

    public void setPlayerBoard(Field[][] playerBoard) {
        this.playerBoard.setBoard(playerBoard);
    }

    public Field[][] getRawBoard() {
        return this.playerBoard.getBoard();
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

        playerBoard.setFieldVal(coord.getCoordX(), coord.getCoordY(), val);
    }

    public int getFieldValue(Coordinate coords) {
        return this.playerBoard.getFieldValue(coords.getCoordX(), coords.getCoordY()); // TODO Change structure of it
    }

    public Region[] getRegions() {
        return playerBoard.getRegions();
    }

    public void setRegions(Region[] regions) {
        this.playerBoard.setRegions(regions);
    }

    public Set<Coordinate> checkSolution() {
        Set<Coordinate> wrongFieldsCoords = new HashSet<>(checkRegions());
        wrongFieldsCoords.addAll(checkHorizontallyAndVertically());
        return wrongFieldsCoords;
    }

    public PlayerMove getUndoMove() throws UnableToUndoMoveException {
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
        playerBoard.setRegions(regions);
        solvedBoard.setRegions(regions);

        solvedBoard.setBoard(createFilledBoard());
        playerBoard.setBoard(getEmptyRawBoard());
        int numbersOfFieldFilledInEachRegion = 3;
        for (Region region : regions) {
            for (int i = 0; i < numbersOfFieldFilledInEachRegion; i++) {
                Coordinate coord = getRandomCoordinateFrom(region);
                if (playerBoard.getFieldValue(coord.getCoordX(), coord.getCoordY()) == 0) {
                    int value = solvedBoard.getFieldValue(coord.getCoordX(), coord.getCoordY());
                    playerBoard.setFieldVal(coord.getCoordX(), coord.getCoordY(), value, false);
                } else {
                    i--;
                }
            }
        }
    }
    private Coordinate getRandomCoordinateFrom(Region region) {
        Random random = new Random();
        int index = random.nextInt(region.getCoords().length);
        return region.getCoords()[index];
    }
    private Field[][] getEmptyRawBoard() {
        Field[][]rawBoard = new Field[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                rawBoard[i][j] = new Field();
            }
        }
        return rawBoard;
    }
    private Field[][] createFilledBoard(){

        Region[] regions = playerBoard.getRegions();
        Field[][] rawBoard = null;

        boolean isBoardFilledCorrectly = false;
        while (!isBoardFilledCorrectly) {
            isBoardFilledCorrectly = true;
            rawBoard = getEmptyRawBoard();
            for (int i = 0; i < 9; i++) {
                if (!fillRegion(rawBoard, regions[i])) {
                    isBoardFilledCorrectly = false;
                    break;
                }
            }
        }

        return rawBoard;

    }

    private boolean fillRegion(Field [][] rawBoard, Region region) {
        ArrayList<Integer> availableValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Random random = new Random();

        for (Coordinate coord : region.getCoords()) {
            ArrayList<Integer> valuesThatCanBeInserted = new ArrayList<>();
            for (int number : availableValues) {
                if (canInsertValue(rawBoard, coord, number)) {
                    valuesThatCanBeInserted.add(number);
                }
            }
            if (valuesThatCanBeInserted.size() == 0) {
                return false;
            }

            int drawnValue = valuesThatCanBeInserted.get(random.nextInt(valuesThatCanBeInserted.size()));
            rawBoard[coord.getCoordY()][coord.getCoordX()].setValue(drawnValue);
            availableValues.remove((Integer)drawnValue);
        }

        return true;

    }

    private boolean canInsertValue(Field [][] rawBoard, Coordinate coord, int val) {
        int sizeBoard = 9;

        for (int i = 0; i < sizeBoard; i++) {
            if (rawBoard[coord.getCoordY()][i].getValue() == val && coord.getCoordX() != i) {
                return false;
            }

            if (rawBoard[i][coord.getCoordX()].getValue() == val && coord.getCoordY() != i) {
                return false;
            }
        }

        return true;
    }

    public FriendlyRepresentField[][] getPlayerBoard() {
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

    public int[][] getSolvedBoard() {
        int[][] values = new int[9][9];

        Field[][] board = solvedBoard.getBoard();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                values[y][x] = board[y][x].getValue();
            }
        }
        return values;
    }
}
