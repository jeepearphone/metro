package entities.station;

import java.util.Objects;

public class StationPair {
    private Station start;
    private Station finish;

    public StationPair(Station start, Station finish){
        this.start = start;
        this.finish = finish;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StationPair that)) return false;
        return (start.getId() == that.start.getId()) && (finish.getId() == that.finish.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start.getId(), finish.getId());
    }
}
