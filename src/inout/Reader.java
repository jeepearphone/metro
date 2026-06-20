package inout;

import java.util.Scanner;

public class Reader {
    private final Scanner scanner;
    public Reader(Scanner scanner){
        this.scanner = scanner;
    }

    public String readString(){
        return scanner.nextLine();
    }
    public int readInt(){
        return Integer.parseInt(scanner.nextLine());
    }
}
