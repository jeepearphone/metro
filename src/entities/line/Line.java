package entities.line;

import entities.station.Station;
import entities.train.Train;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final int id;
    private static int count;
    private final String name;
    List<Station> stations;
    List<Train> trains;

    public Line(String name){
        id = count + 1;
        count++;
        this.name = name;
    }

    public List<Edge> getAllEdges(Station station){
        int index = stations.indexOf(station);
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

    public List<Station> getStations() {
        return stations;
    }

}
