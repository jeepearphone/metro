package inout;

import java.util.List;

/** Разобранная строка ввода. Одинакова и для консоли, и для файла-сценария. */
public record Command(String name, List<String> args) {

    public String arg(int index, String what){
        if (index >= args.size()){
            throw new IllegalArgumentException("Не хватает аргумента: " + what);
        }
        return args.get(index);
    }

    public int intArg(int index, String what){
        String raw = arg(index, what);
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(what + ": ожидалось целое число, а не \"" + raw + "\"");
        }
    }

    public <E extends Enum<E>> E enumArg(int index, Class<E> type, String what){
        String raw = arg(index, what);
        try {
            return Enum.valueOf(type, raw.toUpperCase());
        } catch (IllegalArgumentException e){
            StringBuilder allowed = new StringBuilder();
            for (E constant : type.getEnumConstants()){
                if (!allowed.isEmpty()){
                    allowed.append(", ");
                }
                allowed.append(constant.name());
            }
            throw new IllegalArgumentException(
                    what + ": \"" + raw + "\" не подходит, допустимо: " + allowed);
        }
    }

    public int size(){
        return args.size();
    }
}
