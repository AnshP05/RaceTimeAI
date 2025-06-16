package models;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<RunnerProfile> runners = CSVHandler.readProfilesFromCSV("src/data/GoogleFormData(51).csv");
        TimePredictor predictor = new TimePredictor();

        for (RunnerProfile runner : runners) {
            System.out.println("\nRunner Profile:");
            System.out.println(runner);

            System.out.println("\nPredictions based on " + runner.getRaceDistance() + " in " + Formatter.formatTime(runner.getTotalTimeInSeconds()) + ":");
            List<PredictionResult> predictions = predictor.generatePredictions(runner);

            for (PredictionResult result : predictions) {
                System.out.println(result.getDistance() + " => " + result.getFormattedTime() +
                    " | " + result.getPacePerMile() + " | " + result.getPacePerKm());
            }

            System.out.println("--------------------------------------------------");
        }
    }
}
