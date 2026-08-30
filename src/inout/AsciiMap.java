package inout;

import entities.Stations;
import entities.line.Line;
import entities.station.Station;

import java.util.List;

/**
 * Карта схемы в символах по координатам x/y станций. Зависимостей не требует
 * и сразу показывает, вменяемы ли координаты: от них напрямую зависит время перегона (Edge).
 */
public class AsciiMap {
    private static final int WIDTH = 71;
    private static final int HEIGHT = 25;
    private static final char EMPTY = ' ';
    private static final String MARKERS =
            "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String render(Stations stations, List<Line> lines){
        List<Station> all = stations.getStations();
        if (all.isEmpty()){
            return "Схема пуста: ни одной станции.";
        }
        int minX = all.getFirst().getX();
        int maxX = minX;
        int minY = all.getFirst().getY();
        int maxY = minY;
        for (Station station : all){
            minX = Math.min(minX, station.getX());
            maxX = Math.max(maxX, station.getX());
            minY = Math.min(minY, station.getY());
            maxY = Math.max(maxY, station.getY());
        }

        char[][] grid = new char[HEIGHT][WIDTH];
        for (char[] row : grid){
            java.util.Arrays.fill(row, EMPTY);
        }

        for (Line line : lines){
            List<Station> onLine = line.getStations();
            for (int i = 0; i + 1 < onLine.size(); i++){
                drawSegment(grid,
                        col(onLine.get(i), minX, maxX), row(onLine.get(i), minY, maxY),
                        col(onLine.get(i + 1), minX, maxX), row(onLine.get(i + 1), minY, maxY));
            }
        }

        for (int i = 0; i < all.size(); i++){
            Station station = all.get(i);
            grid[row(station, minY, maxY)][col(station, minX, maxX)] = marker(i);
        }

        StringBuilder result = new StringBuilder();
        for (char[] row : grid){
            result.append(new String(row).stripTrailing()).append('\n');
        }
        result.append("\nОбозначения (x от ").append(minX).append(" до ").append(maxX)
                .append(", y от ").append(minY).append(" до ").append(maxY).append("):\n");
        for (int i = 0; i < all.size(); i++){
            Station station = all.get(i);
            result.append("  ").append(marker(i)).append("  ")
                    .append(station.getName())
                    .append(" (").append(station.getX()).append(", ").append(station.getY()).append(") ")
                    .append(station.getType())
                    .append("  линии: ").append(lineNames(station))
                    .append('\n');
        }
        return result.toString();
    }

    static String lineNames(Station station){
        if (station.getLines().isEmpty()){
            return "нет";
        }
        StringBuilder names = new StringBuilder();
        for (Line line : station.getLines()){
            if (!names.isEmpty()){
                names.append(", ");
            }
            names.append(line.getName());
        }
        return names.toString();
    }

    private static char marker(int index){
        return index < MARKERS.length() ? MARKERS.charAt(index) : '*';
    }

    private int col(Station station, int min, int max){
        return scale(station.getX(), min, max, WIDTH);
    }

    private int row(Station station, int min, int max){
        return scale(station.getY(), min, max, HEIGHT);
    }

    private int scale(int value, int min, int max, int size){
        if (max == min){
            return size / 2;
        }
        return (int) Math.round((double) (value - min) / (max - min) * (size - 1));
    }

    /** Отрезок по Брезенхэму; символ выбирается по наклону, пересечения помечаются '+'. */
    private void drawSegment(char[][] grid, int x0, int y0, int x1, int y1){
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        char symbol = segmentSymbol(x1 - x0, y1 - y0);
        int stepX = x0 < x1 ? 1 : -1;
        int stepY = y0 < y1 ? 1 : -1;
        int error = dx - dy;
        int x = x0;
        int y = y0;
        while (true){
            if (!(x == x0 && y == y0) && !(x == x1 && y == y1)){
                char existing = grid[y][x];
                if (existing == EMPTY){
                    grid[y][x] = symbol;
                }
                else if (existing != symbol){
                    grid[y][x] = '+';
                }
            }
            if (x == x1 && y == y1){
                return;
            }
            int doubleError = error * 2;
            if (doubleError > -dy){
                error -= dy;
                x += stepX;
            }
            if (doubleError < dx){
                error += dx;
                y += stepY;
            }
        }
    }

    private char segmentSymbol(int dx, int dy){
        if (dx == 0){
            return '|';
        }
        if (dy == 0){
            return '-';
        }
        // Экранный y растёт вниз: вправо-вниз даёт '\', вправо-вверх даёт '/'.
        return (dx > 0) == (dy > 0) ? '\\' : '/';
    }
}
