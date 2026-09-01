package entities.route;

import entities.line.Edge;
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

    public int routeMinutes(){
        int minutes = 0;
        for (int i = 0; i + 1 < transitions.size(); i++){
            minutes += new Edge(transitions.get(i), transitions.get(i + 1)).getTimeTo();
        }
        return minutes;
    }
    public List<Station> getTransitions() {
        return new ArrayList<>(transitions);
    }

}
