import inout.Command;
import inout.CommandExecutor;
import inout.CommandParser;
import inout.ConsoleSession;
import inout.Display;
import inout.Reader;
import inout.ScenarioIO;
import metro.MetroSystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path DEMO_SCENARIO = Path.of("scenarios", "demo.metro");

    public static void main(String[] args) {
        Display display = new Display();
        Reader reader = new Reader(new Scanner(System.in, StandardCharsets.UTF_8));

        MetroSystem metro = new MetroSystem();
        CommandParser parser = new CommandParser();
        ScenarioIO scenarioIO = new ScenarioIO(parser);
        CommandExecutor executor = new CommandExecutor(metro, scenarioIO);

        Path scenario = args.length > 0 ? Path.of(args[0]) : DEMO_SCENARIO;
        if (Files.exists(scenario)) {
            try {
                display.println(scenarioIO.load(scenario, executor));
                display.println(executor.execute(new Command("map", List.of())));
            } catch (IOException e) {
                display.println("Не читается файл " + scenario + ": " + e.getMessage());
            }
        }
        else {
            display.println("Файл сценария " + scenario + " не найден — схема пустая, стройте командами.");
        }

        new ConsoleSession(reader, display, parser, executor).run();
    }
}
