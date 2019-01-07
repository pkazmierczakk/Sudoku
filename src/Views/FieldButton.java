package Views;

import Models.Coordinate;

import javax.swing.*;
import java.awt.*;

public class FieldButton extends JButton {
    private Coordinate coord;
    private FriendlyRepresentField field;

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
