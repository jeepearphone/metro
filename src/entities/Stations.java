package entities;

import entities.station.Station;

import java.util.List;

public class Stations {
    private List<Station> stations;
    private double summaryCapability;

    public Station getStationById(Integer id){
        for (Station station : stations){
            if (station.getId() == id){
                return station;
            }
        }
        return stations.getFirst();
    }

    public double getSummaryCapability(){
        return summaryCapability;
    }

    public List<Station> getStations(){
        return stations;
    }


}
