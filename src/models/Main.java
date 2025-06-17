package models;

import java.util.List;

import smile.regression.LinearModel;

public class Main {
    public static void main(String[] args) {
        List<RunnerProfile> runners = CSVHandler.readProfilesFromCSV("src/data/GoogleFormData(52).csv");
        LinearModel model  = MLTrainer.train5kModel(runners);
        for (RunnerProfile runner : runners) {
            if (runner.getRaceTime("5k") == null || runner.getRaceTime("5k").isEmpty()) {
                double predicted = MLTrainer.predict5kTime(runner, model);
                System.out.println("Predicted 5k Time for runner (age " + runner.getAge() + "): " + Formatter.formatTime(predicted));
            }
        }
    }
}
