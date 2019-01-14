package Views;

import Exceptions.UnableToRedoMoveException;
import Exceptions.UnableToUndoMoveException;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuView extends JComponent {
    private final String PREFIX_SAVE_NAME = "gamesave";
    private SudokuView boardView;
    private JButton undoMoveBtn = new JButton(("Undo move"));
    private JButton redoMoveBtn = new JButton("Redo move");
    private JButton checkSolutionBtn = new JButton("Check solution");
    private JButton showSolutionBtn = new JButton("Show solution");
    private JButton helpBtn = new JButton("Help");
    private JButton saveGameBtn = new JButton("Save game");
    private JButton loadGameBtn = new JButton("Load game");
    private final int NUMBER_OF_SAVES_AVAILABLE = 5;

    public MenuView(SudokuGUI sudokuGUI, SudokuView boardView) {
        this.boardView = boardView;
        setLayout(new GridLayout(2,4,5, 5));
        add(helpBtn);
        add(saveGameBtn);
        add(loadGameBtn);
        add(checkSolutionBtn);
        add(undoMoveBtn);
        add(redoMoveBtn);
        add(showSolutionBtn);

        showSolutionBtn.addActionListener(evt -> {
            boardView.showSolution();
            saveGameBtn.setEnabled(false);
            loadGameBtn.setEnabled(false);
            checkSolutionBtn.setEnabled(false);
            undoMoveBtn.setEnabled(false);
            redoMoveBtn.setEnabled(false);
            showSolutionBtn.setEnabled(false);

        });
        checkSolutionBtn.addActionListener(evt -> {
            if (boardView.checkSolution()) {
                JOptionPane.showMessageDialog(boardView, "You won the game!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        undoMoveBtn.addActionListener(evt -> {
            try {
                boardView.undoMove();
            } catch(UnableToUndoMoveException err) {
                JOptionPane.showMessageDialog(boardView,
//                        "Nie można cofnąć ruchu.",
                        "Cannot undo move.",
                        "Warring",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        redoMoveBtn.addActionListener(evt -> {
            try {
                boardView.redoMove();
            } catch (UnableToRedoMoveException err) {
                JOptionPane.showMessageDialog(boardView,
//                        "Nie można ponowić ruchu.",
                        "Cannot redo move.",
                        "Warring",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        helpBtn.addActionListener(evt -> {
            JOptionPane.showMessageDialog(boardView,
                    "Puste pola diagramu należy wypełnić cyframi od 1 do 9 tak, aby:\n" +
                            "\n" +
                            "* w każdym rzędzie i w każdej kolumnie występowało dziewięć różnych cyfr;\n" +
                            "\n" +
                            "* w każdej 9-polowej figurze obwiedzionej grubą linią także znalazło się dziewięć różnych cyfr.",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        saveGameBtn.addActionListener(evt -> {
            Object[] possibilities = new Object[NUMBER_OF_SAVES_AVAILABLE];
            String postfix_saved_game = " (saved)";
            for (int i = 0; i < NUMBER_OF_SAVES_AVAILABLE; i++) {
                possibilities[i] = PREFIX_SAVE_NAME + "-" + (i+1);
                File tmpFile = new File((String)possibilities[i]);
                if (tmpFile.exists()) {
                    possibilities[i] = possibilities[i] + postfix_saved_game;
                }
            }

            String s = (String)JOptionPane.showInputDialog(boardView, "Choose game save", "Save game", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
            if (s != null && !s.isEmpty()) {
                if (s.length() > PREFIX_SAVE_NAME.length()+2) {
                    s = s.substring(0, s.length() - postfix_saved_game.length());
                }

                if (sudokuGUI.saveGame(s)) {
                    JOptionPane.showMessageDialog(boardView, "Game saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(boardView, "Unable to save game!");

                }
            }
        });

        loadGameBtn.addActionListener(evt -> {
            Object[] possibilities = new Object[NUMBER_OF_SAVES_AVAILABLE];
            for (int i = 0; i < NUMBER_OF_SAVES_AVAILABLE; i++) {
                File tmpFile = new File(PREFIX_SAVE_NAME + "-" + (i+1));
                if (tmpFile.exists()) {
                    possibilities[i] = PREFIX_SAVE_NAME + "-" + (i+1);
                }
            }

            String s = (String)JOptionPane.showInputDialog(boardView, "Choose game save", "Load game", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
            if (s != null && !s.isEmpty()) {
                if (sudokuGUI.loadGame(s)) {
                    JOptionPane.showMessageDialog(boardView, "Save load successfully!");
                } else {
                    JOptionPane.showMessageDialog(boardView, "Loading save failed!");
                }
            }
        });
    }
}