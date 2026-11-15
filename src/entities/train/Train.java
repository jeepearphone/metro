package entities.train;

import entities.line.Line;
import entities.passanger.Passanger;
import entities.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private final int id;
    private static final int capability = 15;
    private final Line line;
    private Direction direction;
    private int nextStationIndex;
    private int minutesToNextStation;
    private final List<Passanger> passangers = new ArrayList<>();

    public Train(int id, Line line, Direction direction, int nextStationIndex, int minutesToNextStation){
        this.id = id;
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

    public void changeDirectoty() {
        if (direction == Direction.BACKWARD) {
            direction = Direction.FORWARD;
        } else if (direction == Direction.FORWARD) {
            direction = Direction.BACKWARD;
        }
    }

    public void tick() {
        minutesToNextStation--;
    }

    public void goingTrain() {
        nextStationIndex++;
        if (!line.hasNextStation(nextStationIndex)) {
            changeDirectoty();
        }
        minutesToNextStation = line.getTravelMinutes();
    }

    public Station getNextStation() {
        return line.getStations().get(nextStationIndex);
    }

    public int getCountPassangers(){
        return passangers.size();
    }

    public static int getCapability() {
        return capability;
    }

    public List<Passanger> getPassangers() {
        return List.copyOf(passangers);
    }

    public Direction getDirection() {
        return direction;
    }

    public Line getLine() {
        return line;
    }

    public int getId() {
        return id;
    }

    public int getNextStationIndex() {
        return nextStationIndex;
    }

    public int getMinutesToNextStation() {
        return minutesToNextStation;
    }
}
