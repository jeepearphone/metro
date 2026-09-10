package entities.route;

import entities.line.Edge;
import entities.line.Line;
import entities.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final List<Segment> transitions;
    private int current;
    private final int minutes;

    public Route(List<Segment> transitions, int minutes){
        this.transitions = List.copyOf(transitions);
        this.minutes = minutes;
    }

    public Segment getCurrentSegment(){
        return transitions.get(current);
    }

    public Station getCurrentStation(){
        return getCurrentSegment().stationStart();
    }

    public Station getNextStation(){
        return getCurrentSegment().stationFinish();
    }

    public Line getNextLine(){
        return getCurrentSegment().line();
    }

    public boolean transit(){
        current++;
        return current >= transitions.size();
    }

    public int routeMinutes(){
        return minutes;
    }

    public List<Segment> getTransitions() {
        return new ArrayList<>(transitions);
    }

    public int getCountTransitions(){
        return transitions.size();
    }

}
