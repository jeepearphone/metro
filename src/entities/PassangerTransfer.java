package entities;

import entities.line.Line;
import entities.passanger.Passanger;
import entities.station.Station;
import entities.train.Direction;
import entities.train.Train;

public class PassangerTransfer {
    public void leaveTrain(
            Station station,
            Train train
    ){
        for (Passanger passanger : train.getPassangers()){
            if (passanger.isYourStation(station)){
                train.landingPassangerFromTrain(passanger);
                transit(passanger, station);
            }
        }
    }

    public void transit(Passanger passanger, Station station){
        if (!passanger.transit()){
            station.landingPassangerToStation(passanger);
        }
    }

    public void boardStation(
            Station station,
            Train train
    ){
        Line line = train.getLine();
        for (Passanger passanger : station.getPassangers()){
            if (!passanger.needsLine(line)){
                continue;
            }
            Direction direction = passanger.chooseDirection(line);
            if (direction != train.getDirection()){
                continue;
            }
            station.landingPassangerFromStation(passanger);
            train.landingPassangerToTrain(passanger);
        }
    }
}
