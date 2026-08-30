package metro;

import entities.Stations;
import entities.TypeTime;
import entities.line.Edge;
import entities.line.Line;
import entities.passanger.DestinationSelector;
import entities.passanger.Passanger;
import entities.passanger.PassangerGenerator;
import entities.route.Route;
import entities.route.RouteFinder;
import entities.station.Station;
import entities.station.TypeStation;
import entities.train.Direction;
import entities.train.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MetroSystem {
    private final Stations stations = new Stations();
    private final List<Line> lines = new ArrayList<>();
    private final List<Train> trains = new ArrayList<>();
    private final RouteFinder routeFinder = new RouteFinder();
    private final PassangerGenerator generator;

    public MetroSystem(){
        this(new Random());
    }

    public MetroSystem(Random random){
        this.generator = new PassangerGenerator(new DestinationSelector(stations, random), routeFinder);
    }

    public Line addLine(String name){
        requireText(name, "имя линии");
        if (findLine(name).isPresent()){
            throw new IllegalArgumentException("Линия \"" + name + "\" уже существует");
        }
        Line line = new Line(name);
        lines.add(line);
        return line;
    }

    public Station addStation(int x, int y, String name, TypeStation type){
        requireText(name, "имя станции");
        if (stations.findByName(name).isPresent()){
            throw new IllegalArgumentException("Станция \"" + name + "\" уже существует");
        }
        Station station = new Station(x, y, name, type);
        stations.addStation(station);
        return station;
    }

    public void attach(Line line, Station station){
        if (line.hasStation(station)){
            throw new IllegalArgumentException(
                    "Станция \"" + station.getName() + "\" уже на линии \"" + line.getName() + "\"");
        }
        line.addStation(station);
        station.addLine(line);
        routeFinder.invalidate();
    }

    public Train addTrain(Line line, Direction direction, int nextStationIndex){
        int size = line.getStations().size();
        if (size < 2){
            throw new IllegalArgumentException(
                    "На линии \"" + line.getName() + "\" меньше двух станций, поезду негде ехать");
        }
        if (nextStationIndex < 0 || nextStationIndex >= size){
            throw new IllegalArgumentException(
                    "Индекс станции " + nextStationIndex + " вне линии \"" + line.getName()
                            + "\" (допустимо 0.." + (size - 1) + ")");
        }
        Train train = new Train(line, direction, nextStationIndex, 0);
        line.addTrain(train);
        trains.add(train);
        return train;
    }

    public Optional<Route> findRoute(Station from, Station to){
        return routeFinder.findRoute(from, to);
    }

    public List<Passanger> spawnPassangers(Station station, TypeTime time, int minutes){
        if (minutes <= 0){
            throw new IllegalArgumentException("Отрезок времени должен быть больше нуля");
        }
        int count = (int) Math.round(station.getDemand(time).getGeneratePeople() * minutes);
        List<Passanger> created = generator.generatePassengersOnThisStation(count, station, time);
        for (Passanger passanger : created){
            station.landingPassangerToStation(passanger);
        }
        return created;
    }

    public int routeMinutes(List<Station> path){
        int minutes = 0;
        for (int i = 0; i + 1 < path.size(); i++){
            minutes += new Edge(path.get(i), path.get(i + 1)).getTimeTo();
        }
        return minutes;
    }

    public Optional<Line> findLine(String name){
        for (Line line : lines){
            if (line.getName().equalsIgnoreCase(name)){
                return Optional.of(line);
            }
        }
        return Optional.empty();
    }

    public Line requireLine(String name){
        return findLine(name).orElseThrow(
                () -> new IllegalArgumentException("Нет линии \"" + name + "\""));
    }

    public Station requireStation(String name){
        return stations.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Нет станции \"" + name + "\""));
    }

    public Stations getStations() {
        return stations;
    }

    public List<Line> getLines() {
        return List.copyOf(lines);
    }

    public List<Train> getTrains() {
        return List.copyOf(trains);
    }

    private static void requireText(String value, String what){
        if (value == null || value.isBlank()){
            throw new IllegalArgumentException("Пустое " + what);
        }
    }
}
