package Models;

import java.io.Serializable;

public class Field implements Serializable {
    private int value;
    private boolean isEditable = true;

    public Field() {
        this.value = 0;
        this.isEditable = true;
    }

    public Field(int value, boolean isEditable) {
        this.value = value;
        this.isEditable = isEditable;
    }
    public Field(int value) {
        this.value = value;
        if (value > 0) {
            this.isEditable = false;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
