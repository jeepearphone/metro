package entities.passanger;

import entities.station.Station;

public class Passanger {
    private Station startStation;
    private Station finalStation;

    public Passanger(Station startStation, Station finalStation){
        this.startStation = startStation;
        this.finalStation = finalStation;
    }

}
