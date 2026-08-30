package entities.line;

import entities.station.Station;

public class Edge {
    private static final int MINUTES_PER_STOP = 1;
    private static final int MIN_TRAVEL_MINUTES = 1;

    private final Station stationFrom;
    private final Station stationTo;
    private final int timeTo;

    public Edge(Station stationFrom, Station stationTo){
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.timeTo = calculateTime();
    }

    private int calculateTime(){
        int dx = stationFrom.getX() - stationTo.getX();
        int dy = stationFrom.getY() - stationTo.getY();
        int travel = (int) Math.round(Math.sqrt(dx * dx + dy * dy));
        return Math.max(MIN_TRAVEL_MINUTES, travel) + MINUTES_PER_STOP;
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
