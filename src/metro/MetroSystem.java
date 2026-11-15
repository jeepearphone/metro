package metro;

import entities.MetroMap;
import entities.PassangerTransfer;
import entities.passanger.DestinationSelector;
import entities.passanger.PassangerGenerator;
import entities.route.RouteFinder;

import java.util.Random;

public class MetroSystem {
    private final MetroMap metroMap;
    private final RouteFinder routeFinder;
    private final SimulationSystem simulation;
    private final PassangerTransfer passangerTransfer;

    public MetroSystem(){
        this(new Random());
    }

    public MetroSystem(Random random){
        this.metroMap = new MetroMap();
        this.routeFinder = new RouteFinder(metroMap);
        DestinationSelector selector = new DestinationSelector(metroMap, random);
        PassangerGenerator generator = new PassangerGenerator(selector, routeFinder);
        this.passangerTransfer = new PassangerTransfer();
        this.simulation = new SimulationSystem(generator, passangerTransfer);
    }

    public MetroMap map(){
        return metroMap;
    }

    public RouteFinder routeFinder(){
        return routeFinder;
    }

    public SimulationSystem simulation(){
        return simulation;
    }
}
