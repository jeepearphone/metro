package entities.station;

import entities.line.Line;
import entities.TypeTime;
import entities.passanger.Passanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Station {
    private final int id;
    private static int count;
    private final int x;
    private final int y;
    private final String name;
    private final TypeStation type;
    private List<Line> lines;
    private List<Passanger> passangers;

    public Station(int x, int y, String name, TypeStation type, List<Line> lines){
        id = count + 1;
        this.x = x;
        this.y = y;
        this.name = name;
        this.type = type;
        this.lines = lines;
        count++;
    }

    public void landingPassangerToStation(Passanger passanger){
        passangers.add(passanger);
    }

    public void landingPassangerFromStation(Passanger passanger){
        passangers.remove(passanger);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station station)) return false;
        return id == station.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public StationDemand getDemand(TypeTime time){
        TimesStationBehavior behavior = new TimesStationBehavior(type);
        return behavior.getDemand(time);
    }
    public List<Line> getLines(){
        return lines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public int getCountPassangers(){
        return passangers.size();
    }

    public List<Passanger> getPassangers() {
        return passangers;
    }
}
