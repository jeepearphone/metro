package entities;

import entities.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stations {
    private final List<Station> stations = new ArrayList<>();

    public void addStation(Station station){
        stations.add(station);
    }

    public Optional<Station> findById(int id){
        for (Station station : stations){
            if (station.getId() == id){
                return Optional.of(station);
            }
        }
        return Optional.empty();
    }

    public Optional<Station> findByName(String name){
        for (Station station : stations){
            if (station.getName().equalsIgnoreCase(name)){
                return Optional.of(station);
            }
        }
        return Optional.empty();
    }

    public boolean isEmpty(){
        return stations.isEmpty();
    }

    public int size(){
        return stations.size();
    }

    public List<Station> getStations(){
        return List.copyOf(stations);
    }


}
