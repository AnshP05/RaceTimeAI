package models;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/data/GoogleFormData(52).csv"; // Adjust to your CSV path
        List<RunnerProfile> runners = CSVHandler.readProfilesFromCSV(filePath);

        // Train all pairwise models: input distance → target distance
        Map<String, Map<String, smile.regression.LinearModel>> modelMap = MLTrainer.trainModelsByInputDistance(runners);

        // Example input
        String inputDistance = "mile";
        String inputTime = "4:16";

        // Predict all other distances from this one input
        Map<String, String> predicted = MLTrainer.predictAllFromSingleInput(inputDistance, inputTime, modelMap);

        System.out.println("\nPredicted race times based on " + inputTime + " " + inputDistance + ":");
        for (Map.Entry<String, String> entry : predicted.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
