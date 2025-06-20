package models;

import java.util.*;
import smile.regression.LinearModel;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class ModelEvaluator {

    public static void evaluateMidDistanceModels(List<RunnerProfile> runners, Map<String, Map<String, LinearModel>> models) {
        String[] distances = {"800m", "1500m", "mile", "3000m", "2 mile", "5k", "8k"};
        for(String inputDist : distances) {
            Map<String, LinearModel> innerMap = models.get(inputDist);
            if(innerMap == null) continue;
            for(Map.Entry<String, LinearModel> entry : innerMap.entrySet()) {
                String targetDist = entry.getKey();
                LinearModel model = entry.getValue();

                int count = 0;
                double totalError = 0;
                double totalPercentError = 0;

                for(RunnerProfile runner : runners) {
                    String inputTimeStr = runner.getRaceTime(inputDist);
                    String targetTimeStr = runner.getRaceTime(targetDist);

                    if(inputTimeStr == null || targetTimeStr == null || inputTimeStr.equals("0") || targetTimeStr.equals("0")) continue;

                    double inputTime = RunnerProfile.convertTimeToSeconds(inputTimeStr);
                    double actual = RunnerProfile.convertTimeToSeconds(targetTimeStr);
                    double predicted = model.predict(new double[]{inputTime});

                    double error = Math.abs(predicted - actual);
                    double percentError = (error / actual) * 100;

                    totalError += error;
                    totalPercentError += percentError;
                    count++;
                }

                if(count > 0) {
                    double avgError = totalError / count;
                    double avgPercentError = totalPercentError / count;

                    System.out.printf("From %s to %s: Average Error = %.2f sec, Average %% Error = %.2f%% (%d samples)%n", inputDist, targetDist, avgError, avgPercentError, count);
                }
                
            }
        }
    }

    public static Map<String, String> getBestInputDistancePerTarget(List<RunnerProfile> runners, Map<String, Map<String, LinearModel>> models) {
        String[] distances = {"800m", "1500m", "mile", "3000m", "2 mile", "5k", "8k"};
        Map<String, String> bestInputs = new HashMap<>();
        Map<String, Double> bestErrors = new HashMap<>();

        for(String target : distances) {
            String bestInput = null;
            Double bestAvgPercentError = Double.MAX_VALUE;

            for(String input : distances) {
                double totalError = 0;
                double totalPercentError = 0;
                int count = 0;
                if(input.equals(target)) continue;

                Map<String, LinearModel> inputMap = models.get(input);

                if(inputMap == null || !inputMap.containsKey(target)) continue;

                LinearModel model = inputMap.get(target);

                for(RunnerProfile runner : runners) {
                    String inputTimeStr = runner.getRaceTime(input);
                    String targetTimeStr = runner.getRaceTime(target);
                    if(inputTimeStr == null || targetTimeStr == null || inputTimeStr.equals("0") || targetTimeStr.equals("0")) continue;

                    double inputSeconds = RunnerProfile.convertTimeToSeconds(inputTimeStr);
                    double actualTime = RunnerProfile.convertTimeToSeconds(targetTimeStr);
                    double predicted = model.predict(new double[]{ inputSeconds });

                    double error = Math.abs(predicted - actualTime);
                    double percentError = (error / actualTime) * 100;

                    totalError += error;
                    totalPercentError += percentError;
                    count++;
                }
                double averagePercentError = totalPercentError / count;
                if(averagePercentError < bestAvgPercentError) {
                    bestInput = input;
                    bestAvgPercentError = averagePercentError;
                }

            }

            if(bestInput != null) {
                bestInputs.put(target, bestInput);
                bestErrors.put(target, bestAvgPercentError);
            }
        }
        return bestInputs;
    }
    
}
