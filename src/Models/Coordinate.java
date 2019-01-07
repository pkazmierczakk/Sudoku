package Models;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int coordX;
    private int coordY;

    public Coordinate(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Coordinate coord = (Coordinate) obj;
        return coord.getCoordX() == this.coordX && coord.getCoordY() == this.coordY;
    }

    public String toString() {
        return "("+this.coordX + ", " + this.coordY+")";
    }
}
