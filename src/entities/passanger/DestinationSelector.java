package entities.passanger;

import entities.Stations;
import entities.TypeTime;
import entities.station.Station;
import entities.station.StationDemand;

import java.util.Random;

public class DestinationSelector {
    private Random random = new Random();
    private Stations stations;

    public Station selectDestination(Station startStation, TypeTime time){
        StationDemand demandStartStation = startStation.getDemand(time);
        double summaryCapability = stations.getSummaryCapability() - demandStartStation.getDestinationWeight();
        double randomValue = random.nextDouble() * summaryCapability;
        double currentSummaryWeight = 0;
        for (Station station : stations.getStations()){
            if (station.equals(startStation)){
                continue;
            }
            double weight = station.getDemand(time).getDestinationWeight();
            currentSummaryWeight += weight;
            if (randomValue < currentSummaryWeight) {
                return station;
            }
        }
        return stations.getStations().getFirst();
    }

}
