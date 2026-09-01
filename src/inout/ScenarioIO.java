package inout;

import entities.MetroMap;
import entities.line.Line;
import entities.station.Station;
import entities.train.Train;
import metro.MetroSystem;
import metro.SimulationSystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Файл-сценарий — это записанный скрипт тех же команд, что набираются в консоли,
 * поэтому загрузка идёт через тот же CommandExecutor, а сохранение просто печатает
 * состояние обратно в тот же синтаксис. Такие файлы можно передавать и хранить.
 */
public class ScenarioIO {
    private final CommandParser parser;

    public ScenarioIO(CommandParser parser){
        this.parser = parser;
    }

    /** Ошибочная строка не прерывает загрузку: собираем все проблемы с номерами строк. */
    public String load(Path path, CommandExecutor executor) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int done = 0;
        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < lines.size(); i++){
            try {
                Optional<Command> command = parser.parse(lines.get(i));
                if (command.isEmpty()){
                    continue;
                }
                executor.execute(command.get());
                done++;
            } catch (RuntimeException e){
                errors.append("  строка ").append(i + 1).append(": ").append(e.getMessage()).append('\n');
            }
        }
        StringBuilder report = new StringBuilder();
        report.append("Загружено из ").append(path).append(": команд ").append(done);
        if (!errors.isEmpty()){
            report.append(", с ошибками:\n").append(errors.toString().stripTrailing());
        }
        return report.toString();
    }

    public void save(Path path, MetroMap metroMap, SimulationSystem simulationSystem) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("# Схема метро\n\n");

        for (Line line : metroMap.getLines()){
            out.append("line ").append(CommandParser.quote(line.getName())).append('\n');
        }
        out.append('\n');

        for (Station station : metroMap.getStations()){
            out.append("station ")
                    .append(station.getX()).append(' ')
                    .append(station.getY()).append(' ')
                    .append(CommandParser.quote(station.getName())).append(' ')
                    .append(station.getType()).append('\n');
        }
        out.append('\n');

        // Порядок attach = порядок станций на линии, его нельзя терять при сохранении.
        for (Line line : metroMap.getLines()){
            for (Station station : line.getStations()){
                out.append("attach ")
                        .append(CommandParser.quote(line.getName())).append(' ')
                        .append(CommandParser.quote(station.getName())).append('\n');
            }
            out.append('\n');
        }

        for (Train train : simulationSystem.getTrains()){
            out.append("train ")
                    .append(CommandParser.quote(train.getLine().getName())).append(' ')
                    .append(train.getDirection()).append(' ')
                    .append(train.getNextStationIndex()).append('\n');
        }

        Path parent = path.getParent();
        if (parent != null){
            Files.createDirectories(parent);
        }
        Files.writeString(path, out.toString(), StandardCharsets.UTF_8);
    }
}
