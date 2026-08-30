package inout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CommandParser {

    public Optional<Command> parse(String line){
        List<String> tokens = tokenize(line);
        if (tokens.isEmpty()){
            return Optional.empty();
        }
        String name = tokens.getFirst().toLowerCase();
        List<String> args = new ArrayList<>(tokens.subList(1, tokens.size()));
        return Optional.of(new Command(name, args));
    }

    private List<String> tokenize(String line){
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean started = false;
        for (int i = 0; i < line.length(); i++){
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length() && quoted){
                current.append(line.charAt(++i));
                continue;
            }
            if (c == '"'){
                quoted = !quoted;
                started = true;
                continue;
            }
            if (c == '#' && !quoted){
                break;
            }
            if (Character.isWhitespace(c) && !quoted){
                if (started){
                    tokens.add(current.toString());
                    current.setLength(0);
                    started = false;
                }
                continue;
            }
            current.append(c);
            started = true;
        }
        if (quoted){
            throw new IllegalArgumentException("Незакрытая кавычка");
        }
        if (started){
            tokens.add(current.toString());
        }
        return tokens;
    }

    /** Обратная операция: значение в виде, который сможет прочитать tokenize. */
    public static String quote(String value){
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
