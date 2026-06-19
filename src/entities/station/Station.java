package entities.station;

import entities.Line;
import entities.TypeTime;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private int id;
    private int x;
    private int y;
    private String name;
    private TypeStation type;
    private ArrayList<Line> lines;
    public Station(int id, int x, int y, String name, TypeStation type, ArrayList<Line> lines){
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.type = type;
        this.lines = lines;
    }

    public StationDemand getDemand(TypeTime time){
        TimesStationBehavior behavior = new TimesStationBehavior(type);
        return behavior.getDemand(time);
    }
}
