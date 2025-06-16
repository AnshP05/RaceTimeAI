package models;
import java.util.List;

    public static void main(String[] args) {
        RunnerProfile runner = new RunnerProfile("5k", 0, 15, 30, 50, 3.0);
        TimePredictor predictor = new TimePredictor();
        List<PredictionResult> predictions = predictor.generatePredictions(runner);
        for(PredictionResult prediction : predictions){
            System.out.println("Distance: " + prediction.getDistance());
            System.out.println("Time: " + prediction.getFormattedTime());
            System.out.println("Pace: " + prediction.getPacePerMile() + " | " + prediction.getPacePerKm());
        }
    }
}
