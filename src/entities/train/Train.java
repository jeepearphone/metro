package entities.train;

import entities.line.Line;

public class Train {
    private final int id;
    private static int count;
    private static final int capability = 15;
    private final Line line;
    private Direction direction;
    private int nextStationIndex;
    private int minutesToNextStation;


    public Train(Line line, Direction direction, int nextStationIndex, int minutesToNextStation){
        id = count + 1;
        count++;
        this.line = line;
        this.direction = direction;
        this.nextStationIndex = nextStationIndex;
        this.minutesToNextStation = minutesToNextStation;
    }


}
