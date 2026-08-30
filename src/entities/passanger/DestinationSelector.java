package entities.passanger;

import entities.Stations;
import entities.TypeTime;
import entities.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DestinationSelector {
    private final Random random;
    private final Stations stations;

    public DestinationSelector(Stations stations){
        this(stations, new Random());
    }
    public DestinationSelector(Stations stations, Random random){
        this.stations = stations;
        this.random = random;
    }

    public Optional<Station> selectDestination(Station startStation, TypeTime time){
        List<Station> candidates = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        double summaryWeight = 0;
        for (Station station : stations.getStations()){
            if (station.equals(startStation)){
                continue;
            }
            double weight = station.getDemand(time).getDestinationWeight();
            if (weight <= 0){
                continue;
            }
            candidates.add(station);
            weights.add(weight);
            summaryWeight += weight;
        }
        if (candidates.isEmpty()){
            return Optional.empty();
        }
        double randomValue = random.nextDouble() * summaryWeight;
        double currentSummaryWeight = 0;
        for (int i = 0; i < candidates.size(); i++){
            currentSummaryWeight += weights.get(i);
            if (randomValue < currentSummaryWeight) {
                return Optional.of(candidates.get(i));
            }
        }
        return Optional.of(candidates.getLast());
    }

}
