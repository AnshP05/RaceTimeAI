package models;

import smile.classification.KNN;

public class SmileTest {
    public static void main(String[] args) {
        // Example data: simple 2D points (x, y) and class labels
        double[][] data = {
            {1.0, 2.0},
            {1.5, 1.8},
            {5.0, 8.0},
            {6.0, 9.0},
            {1.0, 0.6},
            {9.0, 11.0}
        };
        int[] labels = {0, 0, 1, 1, 0, 1};

        // Train the k-NN classifier (k=3) using array-based API
        KNN<double[]> knn = KNN.fit(data, labels, 3);

        // Predict for a new data point
        int prediction = knn.predict(new double[]{2.0, 2.0});
        System.out.println("Predicted class: " + prediction);
    }
}
