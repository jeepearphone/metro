package inout;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Display {
    /**
     * Свой поток в UTF-8, а не System.out: консоль Windows берёт кодировку из кодовой
     * страницы и портит кириллические имена станций.
     */
    private final PrintStream out =
            new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

    public void print(String text){
        out.print(text);
    }
    public void println(String text){
        out.println(text);
    }
}
