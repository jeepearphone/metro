package entities.route;

import entities.line.Edge;
import entities.line.Line;
import entities.station.Station;
import entities.station.StationPair;

import java.util.*;

public class RouteFinder {
    private final Map<StationPair, List<Station>> routes = new HashMap<>();

    public Optional<Route> findRoute(
            Station start,
            Station finish
    ){
        if (start.equals(finish)){
            return Optional.empty();
        }
        StationPair key = new StationPair(start, finish);
        List<Station> path = routes.get(key);
        if (path == null){
            path = calculateRoute(start, finish);
            routes.put(key, path);
        }
        if (path.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new Route(new ArrayList<>(path)));
    }

    public void invalidate(){
        routes.clear();
    }

    private List<Station> calculateRoute(
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
                    if (!timeTo.containsKey(neighbor) || newTime < timeTo.get(neighbor)){
                        timeTo.put(neighbor, newTime);
                        previous.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return buildRoute(previous, start, finish);
    }

    private List<Station> buildRoute(
            Map<Station, Station> previous,
            Station start,
            Station finish
    ){
        if (!finish.equals(start) && !previous.containsKey(finish)){
            return List.of();
        }
        List<Station> path = new LinkedList<>();
        Station current = finish;
        while (current != null){
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }
}
