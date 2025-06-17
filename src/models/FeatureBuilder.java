package models;

public class FeatureBuilder {
    public static double[] buildFeatures(RunnerProfile runner) {
        return new double[] {
            runner.getAge(),
            runner.getYearsRunning(),
            runner.getWeeklyMileage(),
            runner.getGender().equalsIgnoreCase("Male") ? 1.0 : 0.0,
            runner.getTotalTimeInSeconds()
        };  
    }
}
