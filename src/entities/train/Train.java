package entities.train;

import entities.line.Line;
import entities.passanger.Passanger;

import java.util.List;

public class Train {
    private final int id;
    private static int count;
    private static final int capability = 15;
    private final Line line;
    private Direction direction;
    private int nextStationIndex;
    private int minutesToNextStation;
    private List<Passanger> passangers;

    public Train(Line line, Direction direction, int nextStationIndex, int minutesToNextStation){
        id = count + 1;
        count++;
        this.line = line;
        this.direction = direction;
        this.nextStationIndex = nextStationIndex;
        this.minutesToNextStation = minutesToNextStation;
    }

    public void landingPassangerToTrain(Passanger passanger){
        passangers.add(passanger);
    }

    public void landingPassangerFromTrain(Passanger passanger){
        passangers.remove(passanger);
    }

    public int getCountPassangers(){
        return passangers.size();
    }

    public static int getCapability() {
        return capability;
    }

    public List<Passanger> getPassangers() {
        return passangers;
    }

    public Direction getDirection() {
        return direction;
    }

    public Line getLine() {
        return line;
    }
}
