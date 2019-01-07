package Views;

import Controllers.SudokuController;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SudokuGUI extends JFrame {
    private SudokuView boardView;
    private SudokuController sudokuController;
    private MenuView menuSudoku;
//    private MenuView menuView;
    public SudokuGUI() {
        sudokuController = new SudokuController();
        boardView = new SudokuView(sudokuController);
        menuSudoku = new MenuView(this, boardView);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mainPanel.add(menuSudoku, BorderLayout.NORTH);
//         region center panel
        JPanel panel = new JPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(10, 10));
        panel.add(boardView, BorderLayout.CENTER);


        add(mainPanel);
        panel.setVisible(true);
        boardView.setVisible(true);
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public boolean saveGame(String filename) {
        try
        {
            FileOutputStream f = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(f);
            os.writeObject(sudokuController);
            f.close();
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean loadGame(String filename) {
        try
        {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            sudokuController = (SudokuController) is.readObject();
            is.close();
            boardView.setSudokuController(sudokuController);

            return true;
        }
        catch (IOException ex){
            System.out.println(ex);
            ex.printStackTrace();
            return false; }
        catch (ClassNotFoundException ex){ return false;}
    }
}
