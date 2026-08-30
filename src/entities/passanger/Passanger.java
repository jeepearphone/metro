package entities.passanger;

import entities.line.Line;
import entities.route.Route;
import entities.station.Station;
import entities.train.Direction;

public class Passanger {
    private final Station startStation;
    private final Station finalStation;
    private final Route route;

    public Passanger(Station startStation, Station finalStation, Route route){
        this.startStation = startStation;
        this.finalStation = finalStation;
        this.route = route;
    }

    public boolean isYourStation(Station station){
        return route.getNextStop().equals(station);
    }

    public boolean needsLine(Line line){
        return line.hasStation(route.getNextStop());
    }

    public boolean transit(){
        return route.transit();
    }

    public Direction chooseDirection(Line line){
        return line.chooseDirection(startStation, route.getNextStop());
    }

    public Route getRoute() {
        return route;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getFinalStation() {
        return finalStation;
    }
}
