package models;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class TimePredictor {
    
    public static final Map<String, Double> distanceMap = Map.of(
        "800m", 800.0,
        "1500m", 1500.0,
        "1600m", 1600.0,
        "mile", 1609.34,
        "3200m", 3200.0,
        "2 mile", 3218.68,
        "5k", 5000.0,
        "8k", 8000.0,
        "10k", 10000.0,
        "half marathon", 21097.5,
        "marathon", 42195.0
    );

    private double manipulateFatigueExponent(RunnerProfile runner) {
        double r = 1.06;
        double yearsRunning = runner.getYearsRunning();
        double weeklyMileage = runner.getWeeklyMileage();
        while(r > 1.035 && yearsRunning >= 0 && weeklyMileage > 30){
            r-= 0.005;
            yearsRunning-=2;
            weeklyMileage -= 10;
        }

        return r;
    }

    private double riegelFormula(double t1, double d1, double d2, double r) {
        return t1 * Math.pow((d2/d1), r);
    }

    public List<PredictionResult> generatePredictions(RunnerProfile runner) {
        List<PredictionResult> results = new ArrayList<>();
        double r = manipulateFatigueExponent(runner);
        double d1 = distanceMap.get(runner.getRaceDistance());
        double t1 = runner.getTotalTimeInSeconds();
        for (Map.Entry<String, Double> entry : distanceMap.entrySet()) {
            if(!entry.getKey().equals(runner.getRaceDistance())){
                results.add(new PredictionResult(entry.getKey(), riegelFormula(t1, d1, entry.getValue(), r)));
            } else {
                results.add(new PredictionResult(entry.getKey(), runner.getTotalTimeInSeconds()));
            }
        }
        return results;
    }
        
}

