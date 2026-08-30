package inout;

import java.util.NoSuchElementException;
import java.util.Optional;

/** Интерактивный источник ввода: тот же парсер и тот же executor, что и у файла-сценария. */
public class ConsoleSession {
    private final Reader reader;
    private final Display display;
    private final CommandParser parser;
    private final CommandExecutor executor;

    public ConsoleSession(Reader reader, Display display, CommandParser parser, CommandExecutor executor){
        this.reader = reader;
        this.display = display;
        this.parser = parser;
        this.executor = executor;
    }

    public void run(){
        display.println("Метро готово. help — список команд, exit — выход.");
        while (!executor.isFinished()){
            display.print("> ");
            String input;
            try {
                input = reader.readString();
            } catch (NoSuchElementException e){
                // Ввод кончился (файл на stdin или Ctrl+D) — это не ошибка, просто конец сессии.
                display.println("");
                return;
            }
            try {
                Optional<Command> command = parser.parse(input);
                if (command.isEmpty()){
                    continue;
                }
                String output = executor.execute(command.get());
                if (!output.isBlank()){
                    display.println(output);
                }
            } catch (RuntimeException e){
                display.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
