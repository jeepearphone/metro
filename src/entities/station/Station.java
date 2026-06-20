package entities.station;

import entities.Line;
import entities.TypeTime;

import java.util.List;

public class Station {
    private final int id;
    private static int count;
    private final int x;
    private final int y;
    private final String name;
    private final TypeStation type;
    private List<Line> lines;
    public Station(int x, int y, String name, TypeStation type, List<Line> lines){
        id = count + 1;
        this.x = x;
        this.y = y;
        this.name = name;
        this.type = type;
        this.lines = lines;
        count++;
    }

    public StationDemand getDemand(TypeTime time){
        TimesStationBehavior behavior = new TimesStationBehavior(type);
        return behavior.getDemand(time);
    }
}
