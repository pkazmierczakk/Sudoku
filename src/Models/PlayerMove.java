package Models;

import java.io.Serializable;

public class PlayerMove implements Serializable {
    private Coordinate coords;
    private int prevValue;

    public PlayerMove(Coordinate coords, int prevValue) {
        this.coords = coords;
        this.prevValue = prevValue;
    }

    public Coordinate getCoords() {
        return coords;
    }

    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(int prevValue) {
        this.prevValue = prevValue;
    }
}
