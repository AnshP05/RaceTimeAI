package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVHandler {

    public static List<RunnerProfile> readProfilesFromCSV(String filePath) {
        List<RunnerProfile> profiles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] csv = line.split(",");
                if (csv.length < 6) {
                    System.out.println("Bad line (too short): " + Arrays.toString(csv));
                    continue;
                }

                try {
                    String gender = csv[2].trim();
                    int age = Integer.parseInt(csv[1].trim());
                    double yearsRunning = Double.parseDouble(csv[3].trim());
                    double weeklyMileage = csv.length > 18 && !csv[18].trim().isEmpty() 
                                           ? Double.parseDouble(csv[18].trim()) : 0.0;
                    String bestRaceDistance = csv[4].trim();
                    double bestRaceTime = RunnerProfile.convertTimeToSeconds(csv[5].trim());

                    RunnerProfile runner = new RunnerProfile(age, gender, yearsRunning, weeklyMileage, bestRaceDistance, bestRaceTime);

                    String[] eventNames = {
                        "800m", "1500m", "mile", "3000m", "2 mile",
                        "5k", "8k", "10k", "half marathon", "marathon"
                    };

                    for (int i = 0; i < eventNames.length; i++) {
                        int index = 6 + i;
                        if (index < csv.length) {
                            String time = csv[index].trim();
                            if (!time.isEmpty()) {
                                runner.addRaceTime(eventNames[i], time);
                            }
                        }
                    }

                    profiles.add(runner);
                } catch (Exception ae) {
                    System.err.println("Bad line (parsing issue): " + Arrays.toString(csv));
                    ae.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return profiles;
    }
}
