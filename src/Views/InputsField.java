package Views;


import javax.swing.*;


public class InputsField extends JComponent {

    private SudokuView board;
    private JPanel block;
    private FieldButton inputButton;
    /** Creates new form Inputs */
    public InputsField(SudokuView board, JPanel block, FieldButton inButton) {
        this.board = board;
        this.block = block;
        this.inputButton = inButton;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form..
     */
    private void initComponents() {
        setLayout(new java.awt.GridLayout(3, 3));

        for (int i = 1; i < 10; i++) {
            JButton inputNum = new JButton();
            inputNum.setBackground(new java.awt.Color(255, 255, 255));
            inputNum.setText(Integer.toString(i));
            inputNum.setAlignmentY(0.0F);
            inputNum.setIconTextGap(1);
            inputNum.setMargin(new java.awt.Insets(2, 2, 2, 2));
            inputNum.setMinimumSize(new java.awt.Dimension(15, 15));
            inputNum.addActionListener(this::setAns);
            add(inputNum);
        }
    }

    private void setAns(java.awt.event.ActionEvent evt){
        board.setInput(evt.getActionCommand(), block, inputButton);
    }

}