package models;

import smile.data.*;
import smile.data.vector.DoubleVector;
import smile.regression.OLS;
import smile.data.formula.Formula;
import smile.regression.LinearModel;

import java.util.*;

public class MLTrainer {

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
}
