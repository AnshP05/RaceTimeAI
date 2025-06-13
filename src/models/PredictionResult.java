package models;

public class PredictionResult {
    //instances
    private String raceDistance;
    private double predictedTimeSeconds;

    public PredictionResult(String raceDistance, double predictedTimeSeconds) {
        this.predictedTimeSeconds = predictedTimeSeconds;
        this.raceDistance = raceDistance;
    }

    public String getDistance() {
        return raceDistance;
    }

    public double getPredictedTimeSeconds() {
        return predictedTimeSeconds;
    }

    public String getFormattedTime() {
        return Formatter.formatTime(predictedTimeSeconds);
    }
}
