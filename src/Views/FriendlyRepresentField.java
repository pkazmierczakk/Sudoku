package Views;

import Models.Coordinate;

public class FriendlyRepresentField {
    private int idRegion;
    private Coordinate coords;
    private int value;
    private boolean isEditable;
    public FriendlyRepresentField(int idRegion, int value,Coordinate coords, boolean isEditable) {
        this.idRegion = idRegion;
        this.coords = coords;
        this.value = value;
        this.isEditable = isEditable;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public String toString() {
        return this.value + " ";
    }

    public Coordinate getCoords() {
        return coords;
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
}