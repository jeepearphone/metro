package entities.route;

import entities.MetroMap;
import entities.line.Edge;
import entities.line.Line;
import entities.station.Station;
import entities.station.StationPair;

import java.util.*;

public class RouteFinder {
    private final MetroMap metroMap;
    private final Map<StationPair, Trip> routes = new HashMap<>();
    private int cachedVersion = -1;
    private static final int walkTransfer = 3;
    private static final int waitTrain = 2;

    public RouteFinder(MetroMap metroMap){
        this.metroMap = metroMap;
    }

    public Optional<Route> findRoute(
            Station start,
            Station finish
    ){
        dropCacheIfMapChanged();
        if (start.equals(finish)){
            return Optional.empty();
        }
        StationPair key = new StationPair(start, finish);
        Trip trip = routes.get(key);
        if (trip == null){
            trip = calculateRoute(start, finish);
            routes.put(key, trip);
        }
        if (trip.segments().isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new Route(trip.segments(), trip.minutes()));
    }

    private void dropCacheIfMapChanged(){
        if (cachedVersion != metroMap.getVersion()){
            routes.clear();
            cachedVersion = metroMap.getVersion();
        }
    }

    private Trip calculateRoute(
            Station start,
            Station finish
    ){
        Map<Node, Integer> timeTo = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<QueueItem> queue = new PriorityQueue<>(
                Comparator.comparingInt(QueueItem::minutes)
        );
        Set<Node> visited = new HashSet<>();
        for(Line line : metroMap.getLinesByStation(start)){
            Node node = new Node(start, line);
            timeTo.put(node, waitTrain);
            queue.add(new QueueItem(node, waitTrain));
        }
        Node arrival = null;
        while (!queue.isEmpty()){
            Node current = queue.poll().node();
            if (!visited.add(current)){
                continue;
            }
            if (current.station().equals(finish)){
                arrival = current;
                break;
            }
            int minutes = timeTo.get(current);
            for (Edge edge : current.line().getAllEdges(current.station())){
                check(new Node(edge.getStationTo(), current.line()),
                        minutes + edge.getTimeTo(),
                        current, timeTo, previous, queue);
            }
            for (Line line : metroMap.getLinesByStation(current.station())){
                if (line.equals(current.line())){
                    continue;
                }
                check(new Node(current.station(), line),
                        minutes + walkTransfer + waitTrain,
                        current, timeTo, previous, queue);
            }
        }
        if (arrival == null){
            return Trip.none();
        }
        return new Trip(buildRoute(previous, arrival), timeTo.get(arrival));
    }

    private void check(
            Node neighbor,
            int newTime,
            Node current,
            Map<Node, Integer> timeTo,
            Map<Node, Node> previous,
            PriorityQueue<QueueItem> queue
    ){
        if (timeTo.containsKey(neighbor) && timeTo.get(neighbor) <= newTime){
            return;
        }
        timeTo.put(neighbor, newTime);
        previous.put(neighbor, current);
        queue.add(new QueueItem(neighbor, newTime));
    }

    private List<Segment> buildRoute(
            Map<Node, Node> previous,
            Node arrival
    ){
        List<Node> nodes = new ArrayList<>();
        for (Node node = arrival; node != null; node = previous.get(node)){
            nodes.add(node);
        }
        Collections.reverse(nodes);
        List<Segment> segments = new ArrayList<>();
        int boarding = 0;
        for (int i = 1; i < nodes.size(); i++){
            if (!nodes.get(i).line().equals(nodes.get(boarding).line())){
                addSegment(segments, nodes.get(boarding), nodes.get(i - 1));
                boarding = i;
            }
        }
        addSegment(segments, nodes.get(boarding), nodes.getLast());
        return segments;
    }

    private void addSegment(List<Segment> segments, Node boarding, Node landing){
        if (boarding.station().equals(landing.station())){
            return;
        }
        segments.add(new Segment(boarding.line(), boarding.station(), landing.station()));
    }

    private record Node (Station station, Line line){}

    private record QueueItem(Node node, int minutes){}

    private record Trip(List<Segment> segments, int minutes){
        static Trip none(){
            return new Trip(List.of(), 0);
        }
    }

}
