package inout;

import entities.TypeTime;
import entities.line.Line;
import entities.passanger.Passanger;
import entities.route.Route;
import entities.station.Station;
import entities.station.TypeStation;
import entities.train.Direction;
import entities.train.Train;
import metro.MetroSystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Единственное место, где команда превращается в вызов MetroSystem.
 * Ошибочная команда возвращает текст ошибки и не роняет сессию.
 */
public class CommandExecutor {
    private final MetroSystem metro;
    private final ScenarioIO scenarioIO;
    private final AsciiMap asciiMap = new AsciiMap();
    private boolean finished;

    public CommandExecutor(MetroSystem metro, ScenarioIO scenarioIO){
        this.metro = metro;
        this.scenarioIO = scenarioIO;
    }

    public String execute(Command command){
        return switch (command.name()){
            case "line" -> line(command);
            case "station" -> station(command);
            case "attach" -> attach(command);
            case "train" -> train(command);
            case "map" -> asciiMap.render(metro.getStations(), metro.getLines());
            case "lines" -> lines();
            case "stations" -> stations();
            case "trains" -> trains();
            case "route" -> route(command);
            case "spawn" -> spawn(command);
            case "load" -> load(command);
            case "save" -> save(command);
            case "help" -> help();
            case "exit", "quit" -> exit();
            default -> throw new IllegalArgumentException(
                    "Неизвестная команда \"" + command.name() + "\", наберите help");
        };
    }

    public boolean isFinished(){
        return finished;
    }

    private String line(Command command){
        Line line = metro.addLine(command.arg(0, "имя линии"));
        return "Линия " + line.getName() + " создана";
    }

    private String station(Command command){
        Station station = metro.addStation(
                command.intArg(0, "координата x"),
                command.intArg(1, "координата y"),
                command.arg(2, "имя станции"),
                command.enumArg(3, TypeStation.class, "тип станции"));
        return "Станция " + station.getName() + " (" + station.getType() + ") создана в точке ("
                + station.getX() + ", " + station.getY() + ")";
    }

    private String attach(Command command){
        Line line = metro.requireLine(command.arg(0, "имя линии"));
        Station station = metro.requireStation(command.arg(1, "имя станции"));
        metro.attach(line, station);
        return station.getName() + " добавлена на линию " + line.getName()
                + " (станций на линии: " + line.getStations().size() + ")";
    }

    private String train(Command command){
        Line line = metro.requireLine(command.arg(0, "имя линии"));
        Direction direction = command.enumArg(1, Direction.class, "направление");
        int index = command.intArg(2, "индекс станции");
        Train train = metro.addTrain(line, direction, index);
        return "Поезд #" + train.getId() + " на линии " + line.getName()
                + ", направление " + direction
                + ", следующая станция: " + line.getStations().get(index).getName();
    }

    private String lines(){
        if (metro.getLines().isEmpty()){
            return "Линий нет";
        }
        StringBuilder result = new StringBuilder();
        for (Line line : metro.getLines()){
            result.append(line.getName()).append(": ");
            List<Station> stations = line.getStations();
            if (stations.isEmpty()){
                result.append("станций нет");
            }
            for (int i = 0; i < stations.size(); i++){
                if (i > 0){
                    result.append(" -> ");
                }
                result.append(stations.get(i).getName());
            }
            result.append("  [поездов: ").append(line.getTrains().size()).append("]\n");
        }
        return result.toString().stripTrailing();
    }

    private String stations(){
        if (metro.getStations().isEmpty()){
            return "Станций нет";
        }
        StringBuilder result = new StringBuilder();
        for (Station station : metro.getStations().getStations()){
            result.append(station.getId()).append("  ")
                    .append(station.getName())
                    .append(" (").append(station.getX()).append(", ").append(station.getY()).append(")  ")
                    .append(station.getType())
                    .append("  линии: ").append(AsciiMap.lineNames(station))
                    .append("  пассажиров: ").append(station.getCountPassangers())
                    .append('\n');
        }
        return result.toString().stripTrailing();
    }

    private String trains(){
        if (metro.getTrains().isEmpty()){
            return "Поездов нет";
        }
        StringBuilder result = new StringBuilder();
        for (Train train : metro.getTrains()){
            List<Station> stations = train.getLine().getStations();
            result.append("#").append(train.getId())
                    .append("  линия ").append(train.getLine().getName())
                    .append("  ").append(train.getDirection())
                    .append("  следующая: ").append(stations.get(train.getNextStationIndex()).getName())
                    .append("  пассажиров: ").append(train.getCountPassangers())
                    .append("/").append(Train.getCapability())
                    .append('\n');
        }
        return result.toString().stripTrailing();
    }

    private String route(Command command){
        Station from = metro.requireStation(command.arg(0, "станция отправления"));
        Station to = metro.requireStation(command.arg(1, "станция назначения"));
        Optional<Route> route = metro.findRoute(from, to);
        if (route.isEmpty()){
            return "Маршрута " + from.getName() + " -> " + to.getName() + " нет: станции не связаны";
        }
        List<Station> path = route.get().getTransitions();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < path.size(); i++){
            if (i > 0){
                result.append(" -> ");
            }
            result.append(path.get(i).getName());
        }
        result.append("\nвсего ").append(metro.routeMinutes(path))
                .append(" мин, станций: ").append(path.size());
        return result.toString();
    }

    private String spawn(Command command){
        Station station = metro.requireStation(command.arg(0, "станция"));
        TypeTime time = command.enumArg(1, TypeTime.class, "время суток");
        int minutes = command.size() > 2 ? command.intArg(2, "минут") : 1;
        List<Passanger> created = metro.spawnPassangers(station, time, minutes);
        if (created.isEmpty()){
            return "На станции " + station.getName() + " (" + time + ", " + minutes
                    + " мин) пассажиров не появилось";
        }
        StringBuilder result = new StringBuilder();
        result.append("На станции ").append(station.getName()).append(" появилось ")
                .append(created.size()).append(" пассажиров:\n");
        for (Passanger passanger : created){
            List<Station> path = passanger.getRoute().getTransitions();
            result.append("  -> ").append(passanger.getFinalStation().getName())
                    .append(" (").append(metro.routeMinutes(path)).append(" мин, станций: ")
                    .append(path.size()).append(")\n");
        }
        return result.toString().stripTrailing();
    }

    private String load(Command command){
        Path path = Path.of(command.arg(0, "путь к файлу"));
        try {
            return scenarioIO.load(path, this);
        } catch (IOException e){
            throw new IllegalArgumentException("Не читается файл " + path + ": " + e.getMessage());
        }
    }

    private String save(Command command){
        Path path = Path.of(command.arg(0, "путь к файлу"));
        try {
            scenarioIO.save(path, metro);
            return "Схема сохранена в " + path;
        } catch (IOException e){
            throw new IllegalArgumentException("Не пишется файл " + path + ": " + e.getMessage());
        }
    }

    private String exit(){
        finished = true;
        return "Выход";
    }

    private String help(){
        return """
                Построение схемы:
                  line <имя>                                создать линию
                  station <x> <y> <имя> <тип>               создать станцию
                  attach <линия> <станция>                  добавить станцию в конец линии
                  train <линия> <FORWARD|BACKWARD> <индекс> поставить поезд на линию
                Просмотр и проверка:
                  map                                       карта схемы
                  lines | stations | trains                 списки
                  route <откуда> <куда>                     кратчайший маршрут
                  spawn <станция> <время> [минут]           создать пассажиров по спросу
                Файлы:
                  load <путь> | save <путь>
                  help | exit
                Типы станций: RESIDENTIAL, BUSINESS, TOURIST, SHOPPING, TRANSPORT_HUB, MIXED
                Время суток:  MORNING, DAY, EVENING, NIGHT
                Имена с пробелами — в кавычках: station 10 5 "Парк Победы" BUSINESS""";
    }
}
