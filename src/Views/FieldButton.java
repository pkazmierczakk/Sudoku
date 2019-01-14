package Views;

import Models.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FieldButton extends JButton {
    private Coordinate coord;
    private FriendlyRepresentField field;
    private ActionListener actionListener;

    FieldButton(Coordinate coord) {
        super();
        this.coord = coord;
    }
    FieldButton() {
        super();
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public void markFieldWrong() {
        if (field.isEditable()) {
            setBorder(BorderFactory.createLineBorder(Color.decode("#FF0000"),5));
        }
    }
    public void unmarkFieldWrong() {
        if (field.isEditable()) {
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        super.addActionListener(actionListener);
    }

    public void removeActionListener() {
        removeActionListener(this.actionListener);
    }

    public void setText(int value) {
        if (value == 0) {
            super.setText("");
        } else {
            super.setText(Integer.toString(value));
        }
    }

    public FriendlyRepresentField getField() {
        return field;
    }

    public void setField(FriendlyRepresentField field) {
        this.field = field;
    }
}
