package models;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/data/GoogleFormData(55).csv";
        List<RunnerProfile> runners1 = CSVHandler.readProfilesFromCSV(filePath);
        List<RunnerProfile> runners2 = CSVHandler.readProfilesFromCSV(filePath);


        MLTrainer.fillMissingTimesWithRiegel(runners1);

        // Train all pairwise models: input distance → target distance
        Map<String, Map<String, smile.regression.LinearModel>> midDistanceMap = MLTrainer.trainMidDistanceModels(runners1);
        Map<String, Map<String, smile.regression.LinearModel>> midDistanceMap1 = MLTrainer.trainMidDistanceModels(runners2);
        // Example input
        String inputDistance = "5k";
        String inputTime = "14:55";

        // Predict all other distances from this one input
        Map<String, String> predicted = MLTrainer.predictAllFromSingleInput(inputDistance, inputTime, midDistanceMap);
        Map<String, String> predicted1 = MLTrainer.predictAllFromSingleInput(inputDistance, inputTime, midDistanceMap1);

        System.out.println("\nPredicted race times based on " + inputTime + " " + inputDistance + ":");
        for (Map.Entry<String, String> entry : predicted.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n(Riegel Adjusted) Predicted race times based on " + inputTime + " " + inputDistance + ":");
        for (Map.Entry<String, String> entry : predicted1.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
