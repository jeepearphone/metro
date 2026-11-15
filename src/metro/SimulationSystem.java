package metro;

import entities.PassangerTransfer;
import entities.TypeTime;
import entities.line.Line;
import entities.passanger.Passanger;
import entities.passanger.PassangerGenerator;
import entities.station.Station;
import entities.train.Direction;
import entities.train.Train;

import java.util.ArrayList;
import java.util.List;

public class SimulationSystem {
    private final PassangerGenerator generator;
    private final PassangerTransfer transfer;
    private final List<Train> trains = new ArrayList<>();
    private int nextTrainId = 1;
    private int currentMinute = 0;

    public SimulationSystem(PassangerGenerator generator, PassangerTransfer transfer){
        this.generator = generator;
        this.transfer = transfer;
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
        Train train = new Train(nextTrainId++, line, direction, nextStationIndex, 0);
        line.addTrain(train);
        trains.add(train);
        return train;
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

    public void tick() {
        for (Train train : trains) {
            train.tick();
            if (train.getMinutesToNextStation() == 0) {
                stoppingTrain(train);
            }
        }
        //passangers
    }

    public void stoppingTrain(Train train) {
        transfer.leaveTrain(train);
        train.goingTrain();
        transfer.boardStation(train);

    }

    public List<Train> getTrains() {
        return List.copyOf(trains);
    }

    public int getCurrentMinute() {
        return currentMinute;
    }
}
