package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import Controllers.SudokuController;
import Exceptions.UnableToRedoMoveException;
import Exceptions.UnableToUndoMoveException;
import Models.Coordinate;
import Models.PlayerMove;


public class SudokuView extends JComponent {
    private final String[] REGION_COLORS = new String[]{"#d4aa7d", "#efd09e", "#d2d8b3", "#90a9b7", "#c5d1eb", "#92afd7", "#5a7684", "#97c695", "#a6d9f7"};
    private SudokuController sudokuController;
    private FieldButton [][]fieldButtons;
    private Set<Coordinate> previousWrongFields = new HashSet<>();

    public SudokuView(SudokuController sudokuController) {
        fieldButtons = new FieldButton[9][9];
        setLayout(new GridLayout(9,9, 4, 4));
        setSudokuController(sudokuController);
    }

    public void setSudokuController(SudokuController sc) {
        removeAll();
        repaint();
        revalidate();
        sudokuController = sc;
        generateBoardView();
    }
    private void generateBoardView() {
        FriendlyRepresentField[][] friendlyBoard = sudokuController.getPlayerBoard();
        for (int coordY = 0; coordY < 9; coordY++) {
            for (int coordX = 0; coordX < 9; coordX++) {
                FieldButton fieldButton = new FieldButton();
                JPanel fblock = new JPanel();
                FriendlyRepresentField field = friendlyBoard[coordY][coordX];

                fieldButton.setField(field);
                decorateFieldBtn(fieldButton, field.getIdRegion());

                if (field.isEditable()) {
                    fieldButton.setText(field.getValue());
                    if (field.getValue() > 0) {
                        fieldButton.setFont(new java.awt.Font("Tahoma", Font.BOLD, 24));
                    }
                    fieldButton.setActionListener(new ClickBoardButtonListener(fblock, fieldButton));
//                    fieldButton.addActionListener(evt -> {
//                        viewInputs(fblock, fieldButton);
//                    });
                } else {
                    fieldButton.setText(field.getValue());
                }
                fieldButton.setCoord(new Coordinate(coordX, coordY));
                fblock.setLayout(new GridLayout(1,1));
                fblock.add(fieldButton);
                add(fblock);

                fieldButtons[coordY][coordX] = fieldButton;
            }
        }
    }

    class ClickBoardButtonListener implements ActionListener {
        private JPanel fblock;
        private FieldButton btn;
        ClickBoardButtonListener(JPanel fblock, FieldButton btn) {
            this.fblock = fblock;
            this.btn = btn;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            viewInputs(fblock, btn);
        }
    }

    private void decorateFieldBtn(FieldButton btn, int region) { //TODO Move to the class ?
        btn.setFont(new Font("Tahoma", Font.PLAIN, 24));
        btn.setBackground(Color.decode(REGION_COLORS[region]));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }


    private void viewInputs(JPanel block, FieldButton inputButtton) {
        InputsField inputs = new InputsField(this, block, inputButtton);
        block.remove(inputButtton);
        block.add(inputs);
        block.revalidate();
//        block.repaint();
    }

    public void setInput(String ans, JPanel block, FieldButton inputButtton) {
        block.removeAll();
        inputButtton.setText(ans);
        sudokuController.setField(inputButtton.getField().getCoords(), Integer.parseInt(ans), true);
        inputButtton.setFont(new java.awt.Font("Tahoma", Font.BOLD, 24));
        block.add(inputButtton);
        block.repaint();
    }

    public void clearWrongFields() {
        for (Coordinate coord : previousWrongFields) {
            fieldButtons[coord.getCoordY()][coord.getCoordX()].unmarkFieldWrong();
        }
    }

    public boolean checkSolution() {
        clearWrongFields();
        Set<Coordinate> wrongFieldsCoords = sudokuController.checkSolution();
        if (wrongFieldsCoords.isEmpty()) {
            wrongFieldsCoords = new HashSet<>(sudokuController.getEmptyFields());
        }
        if (wrongFieldsCoords.size() > 0) {
            for (Coordinate coord : wrongFieldsCoords) {
                fieldButtons[coord.getCoordY()][coord.getCoordX()].markFieldWrong();
            }
            previousWrongFields = wrongFieldsCoords;
            return false;
        }
        return true;
    }

    public void undoMove() throws UnableToUndoMoveException {
        PlayerMove playerMove = sudokuController.getUndoMove();
        fieldButtons[playerMove.getCoords().getCoordY()][playerMove.getCoords().getCoordX()].setText(playerMove.getPrevValue());
    }
    public void redoMove() throws UnableToRedoMoveException {
        PlayerMove playerMove = sudokuController.getRedoMove();
        fieldButtons[playerMove.getCoords().getCoordY()][playerMove.getCoords().getCoordX()].setText(playerMove.getPrevValue());
    }
    public void showSolution() {
        int sizeBoard = 9;
        int [][]validValues = sudokuController.getSolvedBoard();
        for (int y = 0; y < sizeBoard; y++) {
            for (int x = 0; x < sizeBoard; x++) {
                fieldButtons[y][x].setText(validValues[y][x]);
                fieldButtons[y][x].removeActionListener();
                fieldButtons[y][x].setFont(new java.awt.Font("Tahoma", Font.PLAIN, 24));
            }
        }
    }
}