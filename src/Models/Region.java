package Models;

import java.io.Serializable;

public class Region implements Serializable {
    private int idRegion;
    private Coordinate[] coords;

    public Region(int id_region, Coordinate[] coords) {
        this.idRegion = id_region;
        this.coords = coords;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public Coordinate[] getCoords() {
        return coords;
    }

    public void setCoords(Coordinate[] coords) {
        this.coords = coords;
    }
}
