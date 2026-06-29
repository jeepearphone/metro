package entities.route;

import entities.Stations;
import entities.line.Edge;
import entities.line.Line;
import entities.station.Station;
import entities.station.StationPair;

import java.util.*;

public class RouteFinder {
    private Map<StationPair, List<Station>> routes = new HashMap<>();

    public Route findRoute(
            Stations stations,
            Station start,
            Station finish
    ){
        StationPair key = new StationPair(start, finish);
        if (!routes.containsKey(key)){
            Route route = calculateRoute(stations, start, finish);
            routes.put(key, route.getTransitions());
        }
        return new Route(new ArrayList<>(routes.get(key)));
    }

    public Route calculateRoute(
            Stations stations,
            Station start,
            Station finish
    ){
        Map<Station, Integer> timeTo = new HashMap<>();
        Map<Station, Station> previous = new HashMap<>();
        PriorityQueue<Station> queue = new PriorityQueue<>(
                Comparator.comparingInt(
                        s -> timeTo.getOrDefault(s, 7000000))
        );
        Set<Station> visited = new HashSet<>();
        timeTo.put(start, 0);
        queue.add(start);
        while (!queue.isEmpty()){
            Station current = queue.poll();
            if (current.equals(finish)){
                break;
            }
            if (visited.contains(current)){
                continue;
            }
            visited.add(current);
            for (Line line : current.getLines()){
                for (Edge edge : line.getAllEdges(current)){
                    Station neighbor = edge.getStationTo();
                    int newTime = timeTo.get(current) + edge.getTimeTo();
                    if (timeTo.containsKey(neighbor) || newTime < timeTo.get(neighbor)){
                        timeTo.put(neighbor, newTime);
                        previous.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return buildRoute(previous, start, finish);
    }

    public Route buildRoute(
            Map<Station, Station> previous,
            Station start,
            Station finish
    ){
        List<Station> path = new LinkedList<>();
        Station current = finish;
        while (current != null){
            path.add(0, current);
            current = previous.get(current);
        }
        return new Route(path);
    }
}


