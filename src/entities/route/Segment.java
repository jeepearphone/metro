package entities.route;

import entities.line.Line;
import entities.station.Station;

public record Segment(
        Line line,
        Station stationStart,
        Station stationFinish
) {}