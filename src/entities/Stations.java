package entities;

import entities.station.Station;

import java.util.List;

public class Stations {
    private List<Station> stations;
    private double summaryCapability;

    public double getSummaryCapability(){
        return summaryCapability;
    }

    public List<Station> getStations(){
        return stations;
    }
}
