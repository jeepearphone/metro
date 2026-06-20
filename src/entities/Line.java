package entities;

import entities.station.Station;
import entities.train.Train;

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

}
