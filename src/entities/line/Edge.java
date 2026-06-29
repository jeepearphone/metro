package entities.line;

import entities.station.Station;

public class Edge {
    private Station stationFrom;
    private Station stationTo;
    private int timeTo;

    public Edge(Station stationFrom, Station stationTo){
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.timeTo = calculateTime();
    }

    public int calculateTime(){
        return (int)Math.round(Math.sqrt(
                (stationFrom.getX() - stationTo.getX()) * (stationFrom.getX() - stationTo.getX()) +
                        (stationFrom.getY() - stationTo.getY()) * (stationFrom.getY() - stationTo.getY())
        ));
    }

    public Station getStationFrom(){
        return stationFrom;
    }

    public Station getStationTo() {
        return stationTo;
    }

    public int getTimeTo() {
        return timeTo;
    }
}
