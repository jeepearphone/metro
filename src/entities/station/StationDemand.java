package entities.station;

public class StationDemand {
    double generatePeople;
    double destinationWeight;
    public StationDemand(double generatePeople, double destinationWeight){
        this.generatePeople = generatePeople;
        this.destinationWeight = destinationWeight;
    }

    public double getGeneratePeople() {
        return generatePeople;
    }

    public double getDestinationWeight() {
        return destinationWeight;
    }
}
