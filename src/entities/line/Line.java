package entities.line;

import entities.route.Segment;
import entities.station.Station;
import entities.train.Direction;
import entities.train.Train;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final int id;
    private final String name;
    private final List<Station> stations = new ArrayList<>();
    private final List<Train> trains = new ArrayList<>();

    public Line(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void addStation(Station station){
        stations.add(station);
    }

    public void addTrain(Train train){
        trains.add(train);
    }

    public List<Edge> getAllEdges(Station station){
        int index = requireIndex(station);
        List<Edge> edges = new ArrayList<>();
        if (index > 0){
            Station prev = stations.get(index - 1);
            Edge edge = new Edge(station, prev);
            edges.add(edge);
        }
        if (index + 1 < stations.size()){
            Station next = stations.get(index + 1);
            Edge edge = new Edge(station, next);
            edges.add(edge);
        }
        return edges;
    }

    public Direction chooseDirection(Station stationFrom, Station stationTo){
        int indexFrom = requireIndex(stationFrom);
        int indexTo = requireIndex(stationTo);
        if (indexFrom < indexTo) {
            return Direction.FORWARD;
        }
        else {
            return Direction.BACKWARD;
        }
    }

    public boolean hasStation(Station station){
        return stations.contains(station);
    }
    
    private int requireIndex(Station station){
        int index = stations.indexOf(station);
        if (index < 0){
            throw new IllegalArgumentException(
                    "Станция \"" + station.getName() + "\" не на линии \"" + name + "\"");
        }
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line line)) return false;
        return id == line.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }

    public List<Train> getTrains() {
        return List.copyOf(trains);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
