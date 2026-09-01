package entities;

import entities.line.Line;
import entities.station.Station;
import entities.station.TypeStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MetroMap {
    private final Stations stations = new Stations();
    private final List<Line> lines = new ArrayList<>();
    private Map<Station, List<Line>> linesByStation;
    private int version;
    private int nextLineId = 1;
    private int nextStationId = 1;

    public Line addLine(String name){
        requireText(name, "имя линии");
        if (findLine(name).isPresent()){
            throw new IllegalArgumentException("Линия \"" + name + "\" уже существует");
        }
        Line line = new Line(nextLineId++, name);
        lines.add(line);
        changed();
        return line;
    }

    public Station addStation(int x, int y, String name, TypeStation type){
        requireText(name, "имя станции");
        if (stations.findByName(name).isPresent()){
            throw new IllegalArgumentException("Станция \"" + name + "\" уже существует");
        }
        Station station = new Station(nextStationId++, x, y, name, type);
        stations.addStation(station);
        changed();
        return station;
    }

    public void attach(Line line, Station station){
        if (line.hasStation(station)){
            throw new IllegalArgumentException(
                    "Станция \"" + station.getName() + "\" уже на линии \"" + line.getName() + "\"");
        }
        line.addStation(station);
        changed();
    }

    public List<Line> getLinesByStation(Station station){
        if (linesByStation == null){
            rebuild();
        }
        return linesByStation.getOrDefault(station, List.of());
    }

    public int getVersion(){
        return version;
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

    public Optional<Station> findStation(String name){
        return stations.findByName(name);
    }

    public Station requireStation(String name){
        return stations.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Нет станции \"" + name + "\""));
    }

    public List<Station> getStations(){
        return stations.getStations();
    }

    public List<Line> getLines() {
        return List.copyOf(lines);
    }

    private void changed(){
        linesByStation = null;
        version++;
    }

    private void rebuild(){
        linesByStation = new HashMap<>();
        for (Line line : lines){
            for (Station station : line.getStations()){
                if (!linesByStation.containsKey(station)){
                    linesByStation.put(station, new ArrayList<>());
                }
                linesByStation.get(station).add(line);
            }
        }
    }

    private static void requireText(String value, String what){
        if (value == null || value.isBlank()){
            throw new IllegalArgumentException("Пустое " + what);
        }
    }
}
