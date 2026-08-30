package entities.passanger;

import entities.TypeTime;
import entities.route.Route;
import entities.route.RouteFinder;
import entities.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassangerGenerator {
    private final DestinationSelector selector;
    private final RouteFinder routeFinder;

    public PassangerGenerator(DestinationSelector selector, RouteFinder routeFinder){
        this.selector = selector;
        this.routeFinder = routeFinder;
    }

    public List<Passanger> generatePassengersOnThisStation(int count, Station startStation, TypeTime time){
        List<Passanger> passangers = new ArrayList<>();
        for (int i = 0; i < count; i++){
            Optional<Station> finalStation = selector.selectDestination(startStation, time);
            if (finalStation.isEmpty()){
                continue;
            }
            Optional<Route> route = routeFinder.findRoute(startStation, finalStation.get());
            if (route.isEmpty()){
                continue;
            }
            passangers.add(new Passanger(startStation, finalStation.get(), route.get()));
        }
        return passangers;
    }
}
