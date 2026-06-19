package entities;

public enum TypeTime {
    MORNING,
    DAY,
    EVENING,
    NIGHT;
    public static TypeTime timePeriod(int hour) {
        if (hour >= 6 && hour < 10) {
            return MORNING;
        }
        if (hour >= 10 && hour < 18) {
            return DAY;
        }
        if (hour >= 18 && hour < 24) {
            return EVENING;
        }
        return NIGHT;
    }
}
