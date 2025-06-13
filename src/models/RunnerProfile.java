package models;

public class RunnerProfile {
    //instances
    private String raceDistance;
    private int raceHours;
    private int raceMinutes;
    private int raceSeconds;
    private int weeklyMileage;
    private double yearsRunning;

    //constructor
    public RunnerProfile(String raceDistance, int raceHours, int raceMinutes, int raceSeconds, int weeklyMileage, double yearsRunning) {
        this.raceDistance = raceDistance;
        this.raceHours = raceHours;
        this.raceMinutes = raceMinutes;
        this.raceSeconds = raceSeconds;
        this.weeklyMileage = weeklyMileage;
        this.yearsRunning = yearsRunning;
    }

    // Getters and setters
    public String getRaceDistance() {
        return raceDistance;
    }

    public void setRaceDistance(String raceDistance) {
        this.raceDistance = raceDistance;
    }

    public int getRaceHours() {
        return raceHours;
    }

    public void setRaceHours(int raceHours) {
        this.raceHours = raceHours;
    }

    public int getRaceMinutes() {
        return raceMinutes;
    }

    public void setRaceMinutes(int raceMinutes) {
        this.raceMinutes = raceMinutes;
    }

    public int getRaceSeconds() {
        return raceSeconds;
    }

    public void setRaceSeconds(int raceSeconds) {
        this.raceSeconds = raceSeconds;
    }

    public int getWeeklyMileage() {
        return weeklyMileage;
    }

    public void setWeeklyMileage(int weeklyMileage) {
        this.weeklyMileage = weeklyMileage;
    }

    public double getYearsRunning() {
        return yearsRunning;
    }

    public void setYearsRunning(double yearsRunning) {
        this.yearsRunning = yearsRunning;
    }

    //helper methods
    public double getTotalTimeInSeconds() {
        return raceSeconds + 60 * raceMinutes + 3600 * raceHours;
    }
}
