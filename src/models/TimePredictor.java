package models;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class TimePredictor {
    
    public static final Map<String, Double> distanceMap = Map.ofEntries(
        Map.entry("800m", 800.0),
        Map.entry("1500m", 1500.0),
        Map.entry("1600m", 1600.0),
        Map.entry("mile", 1609.34),
        Map.entry("3200m", 3200.0),
        Map.entry("2 mile", 3218.68),
        Map.entry("5k", 5000.0),
        Map.entry("8k", 8000.0),
        Map.entry("10k", 10000.0),
        Map.entry("half marathon", 21097.5),
        Map.entry("marathon", 42195.0)
    );


    public double manipulateFatigueExponent(RunnerProfile runner) {
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
        String baseRace = runner.getRaceDistance();

        if (baseRace == null || !distanceMap.containsKey(baseRace)) {
            System.err.println("Missing or invalid race distance for runner: " + baseRace);
            return results; // Or handle fallback logic
        }

        double d1 = distanceMap.get(baseRace);
        double t1 = runner.getTotalTimeInSeconds();
        double r = manipulateFatigueExponent(runner);

        for (Map.Entry<String, Double> entry : distanceMap.entrySet()) {
            String event = entry.getKey();
            double d2 = entry.getValue();

            if (!event.equals(baseRace)) {
                results.add(new PredictionResult(event, riegelFormula(t1, d1, d2, r)));
            } else {
                results.add(new PredictionResult(event, t1));
            }
        }

        return results;
    }

        
}

