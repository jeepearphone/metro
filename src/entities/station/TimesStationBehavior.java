package entities.station;

import entities.TypeTime;


public class TimesStationBehavior {
    private final TypeStation typeStation;

    public TimesStationBehavior(TypeStation typeStation){
        this.typeStation = typeStation;
    }

    public StationDemand getDemand(TypeTime time) {

        return switch (typeStation) {
            case RESIDENTIAL -> residentialDemand(time);
            case BUSINESS -> businessDemand(time);
            case TOURIST -> touristicDemand(time);
            case TRANSPORT_HUB -> transHubDemand(time);
            case SHOPPING -> shoppingDemand(time);
            case MIXED -> mixedDemand(time);
        };
    }

    private StationDemand residentialDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(2.2, 0.4);
            case DAY -> new StationDemand(0.7, 0.7);
            case EVENING -> new StationDemand(0.6, 2.1);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }

    private StationDemand businessDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(0.5, 2.4);
            case DAY -> new StationDemand(1.0, 1.4);
            case EVENING -> new StationDemand(2.1, 0.5);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }

    private StationDemand touristicDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(0.8, 1.5);
            case DAY -> new StationDemand(1.4, 1.7);
            case EVENING -> new StationDemand(1.2, 1.3);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }

    private StationDemand transHubDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(1.5, 1.5);
            case DAY -> new StationDemand(1.2, 1.2);
            case EVENING -> new StationDemand(1.6, 1.6);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }
    private StationDemand shoppingDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(0.4, 0.7);
            case DAY -> new StationDemand(1.3, 1.5);
            case EVENING -> new StationDemand(1.4, 1.5);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }

    private StationDemand mixedDemand(TypeTime time) {
        return switch (time) {
            case MORNING -> new StationDemand(1.0, 1.0);
            case DAY -> new StationDemand(1.1, 1.1);
            case EVENING -> new StationDemand(1.3, 1.3);
            case NIGHT -> new StationDemand(0.0, 0.0);
        };
    }
}
