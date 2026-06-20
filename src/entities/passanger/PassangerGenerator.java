package entities.passanger;

import entities.TypeTime;
import entities.station.Station;

import java.util.ArrayList;
import java.util.List;

public class PassangerGenerator {
    private DestinationSelector selector;

    public List<Passanger> generatePassengers(int count, Station startStation, TypeTime time){
        List<Passanger> passangers = new ArrayList<>();
        for (int i = 0; i < count; i++){
            Station finalStation = selector.selectDestination(startStation, time);
            passangers.add(new Passanger(startStation, finalStation));
        }
        return passangers;
    }
}
