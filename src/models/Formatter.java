package models;

public class Formatter {
    
    public static String formatTime(double seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int secs = (int) (seconds % 60);
        if(hours == 0) return minutes + ":" + String.format("%02d", secs);
        return hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", secs);
    }

    public static String formatPacePerMile(double seconds, double distanceMeters) {
        double distanceMiles = distanceMeters/1609.34;
        double secondsPerMile = seconds / distanceMiles;
        return formatTime(secondsPerMile) + " / mile";
    }

    public static String formatPacePerKm(double seconds, double distanceMeters) {
        double distanceKm = distanceMeters / 1000;
        double secondsPerKm = seconds / distanceKm;
        return formatTime(secondsPerKm) + " / km";
    }
}
