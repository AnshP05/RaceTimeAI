package models;

import smile.data.*;
import smile.data.vector.DoubleVector;
import smile.regression.OLS;
import smile.regression.RidgeRegression;
import smile.data.formula.Formula;
import smile.regression.LinearModel;

import java.util.*;

import scala.Array;

public class MLTrainer {

    public static void fillMissingTimesWithRiegel(List<RunnerProfile> runners) {
        String[] distances = {"800m", "1500m", "mile", "3000m", "2 mile", "5k", "8k", "10k", "half marathon", "marathon"};

        for(RunnerProfile runner : runners) {
            String baseEvent = runner.getRaceDistance();
            String baseTimeStr = runner.getRaceTime(baseEvent);

            if (baseEvent == null || baseTimeStr == null || baseTimeStr.isEmpty() || baseTimeStr.equals("0")) continue;

            double t1 = RunnerProfile.convertTimeToSeconds(baseTimeStr);
            double d1 = TimePredictor.distanceMap.getOrDefault(baseEvent, -1.0);

            if(d1 <= 0) continue;

            double r = new TimePredictor().manipulateFatigueExponent(runner);

            for(String targetEvent : distances) {
                if(targetEvent.equals(baseEvent)) continue;

                String existingTime = runner.getRaceTime(targetEvent);
                if (existingTime != null && !existingTime.isEmpty() && !existingTime.equals("0")) continue;


                double d2 = TimePredictor.distanceMap.getOrDefault(targetEvent, -1.0);
                if(d2 <= 0) continue;

                double t2 = t1 * Math.pow((d2/d1), r);
                String formatted = Formatter.formatTime(t2);

                runner.addRaceTime(targetEvent, formatted);
            }
        }
    }

    public static smile.regression.LinearModel train5kModel(List<RunnerProfile> runners) {
        List<double[]> X = new ArrayList<>();
        List<Double> Y = new ArrayList<>();

        for (RunnerProfile r : runners) {
            String timeStr = r.getRaceTime("5k");
            if (timeStr != null && !timeStr.isEmpty()) {
                X.add(FeatureBuilder.buildFeatures(r)); 
                Y.add(RunnerProfile.convertTimeToSeconds(timeStr));
            }
        }
        if (X.isEmpty()) {
            System.out.println("No 5k data!");
            return null;
        }

        double[][] x = X.toArray(new double[0][]);
        double[] y = Y.stream().mapToDouble(Double::valueOf).toArray();

        DataFrame df = DataFrame.of(x, "age", "yearsRunning", "weeklyMileage", "isMale").merge(DoubleVector.of("fiveKTime", y));
;

        return smile.regression.RidgeRegression.fit(Formula.lhs("fiveKTime"), df, 0.1);

    }

    public static double predict5kTime(RunnerProfile runner, LinearModel model) {

        double[] features = FeatureBuilder.buildFeatures(runner);
        return model.predict(features);

    }

    public static Map<String, LinearModel> trainAllModels(List<RunnerProfile> runners) {
        String[] allDistances = {"800m", "1500m", "mile", "3000m", "2 mile", "5k", "8k", "10k", "half marathon", "marathon"};

        Map<String, LinearModel> models = new HashMap<>();

        for(String target : allDistances) {
            List<double[]> featuresList = new ArrayList<>();
            List<Double> labelList = new ArrayList<>();

            for(RunnerProfile r : runners) {
                String targetTimeStr = r.getRaceTime(target);
                if(targetTimeStr == null || targetTimeStr.isEmpty()) continue;

                double[] features = new double[allDistances.length - 1];
                boolean valid = true;
                int index = 0;

                for(String dist : allDistances) {
                    if(dist.equals(target)) continue;
                    String timeStr = r.getRaceTime(dist);
                    if (timeStr == null || timeStr.isEmpty()) {
                        features[index++] = 0; // or maybe skip? up to you
                    } else {
                        features[index++] = RunnerProfile.convertTimeToSeconds(timeStr);
                    }

                }

                featuresList.add(features);
                labelList.add(RunnerProfile.convertTimeToSeconds(targetTimeStr));
            }

            if (!featuresList.isEmpty()) {
                double[][] x = featuresList.toArray(new double[0][]);
                double[] y = labelList.stream().mapToDouble(Double::doubleValue).toArray();

                String[] featureNames = Arrays.stream(allDistances)
                                          .filter(d -> !d.equals(target))
                                          .toArray(String[]::new);

                DataFrame df = DataFrame.of(x, featureNames).merge(DoubleVector.of(target, y));
                LinearModel model = RidgeRegression.fit(Formula.lhs(target), df, 0.1);
                models.put(target, model);
            }
        }

        return models;
    }

   public static Map<String, Map<String, LinearModel>> trainModelsByInputDistance(List<RunnerProfile> runners) {
        String[] allDistances = {
            "800m", "1500m", "mile", "3000m", "2 mile",
            "5k", "8k", "10k", "half marathon", "marathon"
        };

        Map<String, Map<String, LinearModel>> modelMap = new HashMap<>();

        for (String inputDist : allDistances) {
            for (String targetDist : allDistances) {
                if (inputDist.equals(targetDist)) continue;

                List<Double> xList = new ArrayList<>();
                List<Double> yList = new ArrayList<>();

                for (RunnerProfile r : runners) {
                    String inputTimeStr = r.getRaceTime(inputDist);
                    String targetTimeStr = r.getRaceTime(targetDist);

                    if (inputTimeStr == null || targetTimeStr == null ||
                        inputTimeStr.isEmpty() || targetTimeStr.isEmpty()) {
                        continue;
                    }

                    double inputTime = RunnerProfile.convertTimeToSeconds(inputTimeStr);
                    double targetTime = RunnerProfile.convertTimeToSeconds(targetTimeStr);

                    // Filter out obviously bogus or unhelpful data
                    if (inputTime < 30 || inputTime > 3600 || targetTime < 30 || targetTime > 20000) {
                        continue;
                    }

                    xList.add(inputTime);
                    yList.add(targetTime);
                }

                if (xList.size() < 10) {
                    System.out.println("Skipping model from " + inputDist + " to " + targetDist + " (not enough data)");
                    continue;
                }

                double[] xArr = xList.stream().mapToDouble(Double::doubleValue).toArray();
                double[] yArr = yList.stream().mapToDouble(Double::doubleValue).toArray();

                DataFrame df = DataFrame.of(DoubleVector.of(inputDist, xArr)).merge(DoubleVector.of(targetDist, yArr));

                try {
                    LinearModel model = RidgeRegression.fit(Formula.lhs(targetDist), df, 0.1);
                    modelMap.computeIfAbsent(inputDist, k -> new HashMap<>()).put(targetDist, model);
                    System.out.println("Trained model from " + inputDist + " to " + targetDist + " with " + xArr.length + " samples.");
                } catch (Exception e) {
                    System.err.println("Error training model from " + inputDist + " to " + targetDist + ": " + e.getMessage());
                }
            }
        }

        return modelMap;
    }


    public static Map<String, String> predictAllFromSingleInput(String inputDistance, String inputTimeStr, Map<String, Map<String, LinearModel>> modelMap) {
        Map<String, String> predictions = new HashMap<>();

        double inputTimeSeconds = RunnerProfile.convertTimeToSeconds(inputTimeStr);

        Map<String, LinearModel> modelsForInput = modelMap.get(inputDistance);
        if(modelsForInput == null) {
            System.out.println("No models found for input distances: " + inputDistance);
            return predictions;
        }

        for(Map.Entry<String, LinearModel> entry : modelsForInput.entrySet()) {
            String targetDistance = entry.getKey();
            LinearModel model = entry.getValue();
            
            double predictedSeconds = model.predict(new double[]{inputTimeSeconds});
            String formatted = Formatter.formatTime(predictedSeconds);
            predictions.put(targetDistance, formatted);
        }

        return predictions;
    }

    public static Map<String, Map<String, LinearModel>> trainMidDistanceModels(List<RunnerProfile> runners) {
        String[] midDistanceEvents = {"800m", "1500m", "mile", "3000m", "2 mile", "5k", "8k"};
        Map<String, Map<String, LinearModel>> midModels = new HashMap<>();

        for(String inputDist : midDistanceEvents) {
            for(String targetDist : midDistanceEvents) {
                if(inputDist.equals(targetDist)) continue;

                List<Double> xList = new ArrayList<>();
                List<Double> yList = new ArrayList<>();

                for(RunnerProfile runner: runners) {
                    String inputTimeStr = runner.getRaceTime(inputDist);
                    String targetTimeStr = runner.getRaceTime(targetDist);

                    if(inputDist == null || targetDist == null || inputDist.isEmpty() || targetDist.isEmpty() || inputDist.equals("0") || targetDist.equals("0")) {
                        continue;
                    }

                    double inputTime = RunnerProfile.convertTimeToSeconds(inputTimeStr);
                    double targetTime = RunnerProfile.convertTimeToSeconds(targetTimeStr);

                    xList.add(inputTime);
                    yList.add(targetTime);
                }

                if(xList.size() < 10) continue;

                double[] xArr = xList.stream().mapToDouble(Double :: doubleValue).toArray();
                double[] yArr = yList.stream().mapToDouble(Double :: doubleValue).toArray();

                DataFrame df = DataFrame.of(DoubleVector.of(inputDist, xArr)).merge(DoubleVector.of(targetDist, yArr));
                LinearModel model = RidgeRegression.fit(Formula.lhs(targetDist), df, 0.1);
                midModels.computeIfAbsent(inputDist, k -> new HashMap<>()).put(targetDist, model);

            }
        }
        return midModels;
    }
}
