package entities.route;

import entities.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Station> transitions;

    public Route(List<Station> transitions){
        this.transitions = transitions;
    }

    public boolean transit(){
        transitions.removeFirst();
        return transitions.isEmpty();
    }

    public Station getNextStop(){
        return transitions.getFirst();
    }

    public List<Station> getTransitions() {
        return new ArrayList<>(transitions);
    }

}
