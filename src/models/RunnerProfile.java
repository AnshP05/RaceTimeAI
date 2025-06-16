package models;

import java.util.*;

public class RunnerProfile {
    //instances
    private int age;
    private String gender;
    private double yearsRunning;
    private double weeklyMileage;
    private Map<String, String> raceTimes;
    private String bestRaceDistance;
    private double bestRaceTime;


    public RunnerProfile(int age, String gender, double yearsRunning, double weeklyMileage, String bestRaceDistance, double bestRaceTime) {
        this.age = age;
        this.gender = gender;
        this.yearsRunning = yearsRunning;
        this.weeklyMileage = weeklyMileage;
        this.raceTimes = new HashMap<>();
        this.bestRaceDistance = bestRaceDistance;
        this.bestRaceTime = bestRaceTime;

    }

    public void addRaceTime(String race, String time){
        raceTimes.put(race, time);
    }

    @Override
    public String toString() {
        return "RunnerProfile{" +
            "age=" + age +
            ", gender='" + gender + '\'' +
            ", yearsRunning=" + yearsRunning +
            ", weeklyMileage=" + weeklyMileage +
            ", raceTimes=" + raceTimes +
            '}';
    }

    //getters

    public double getYearsRunning() {
    return yearsRunning;
    }

    public double getWeeklyMileage() {
        return weeklyMileage;
    }

    public Map<String, String> getRaceTimes() {
        return raceTimes;
    }

    public String getRaceDistance() {
        return bestRaceDistance;
    }

    public double getTotalTimeInSeconds() {
        return bestRaceTime;
    }


    public static double convertTimeToSeconds(String time) {
        String[] parts = time.split(":");

        if (parts.length == 2) {
            // mm:ss.xx
            int minutes = Integer.parseInt(parts[0]);
            double seconds = Double.parseDouble(parts[1]);
            return minutes * 60 + seconds;

        } else if (parts.length == 3) {
            // hh:mm:ss
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            double seconds = Double.parseDouble(parts[2]);
            return hours * 3600 + minutes * 60 + seconds;

        } else {
            return 0;
        }
    }


}
