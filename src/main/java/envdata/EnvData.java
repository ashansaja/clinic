/**
 * EnvData implements the Lake Pend Oreille data analysis challenge as outlined
 * in the code clinics at LinkedIn Learning at
 * https://www.linkedin.com/learning/code-clinic-java
 *
 *
 * This is just this authors implementation with some additional data analysis
 * over and above the mean and median computed in the LinkedIn version. The most
 * frequent reading along with the frequency and the highest reading is also
 * analyzed and reported in this version.
 *
 * @author Ashwin Rao
 */

package envdata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvData {

    /**
     * Returns a sorted map from the provided map of weather data reading values
     * and their respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return Map<Double, Integer>
     */
    Map<Double, Integer> sortMap(Map<Double, Integer> m) {
        // Convert to TreeMap to get sorted map
        if (!(m instanceof TreeMap)) {
            m = new TreeMap<>(m);
        }
        return m;
    }

    /**
     * Gets the keys form a map of weather data reading values and their
     * respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return List<Double>
     */
    List<Double> getKeysFromValueMap(Map<Double, Integer> m) {
        // Extract keys into a list
        List<Double> keysList;
        keysList = new ArrayList<>();
        m.entrySet().forEach((e) -> {
            for (int i = 0; i < e.getValue(); i++) {
                keysList.add(e.getKey());
            }
        });
        return keysList;
    }

    /**
     * Gets the keys from a map of weather data reading counts and their
     * respective list of weather reading readings.
     *
     * @param m Map that stores the reading counts and their respective values
     * as a list
     * @return List<Integer>
     */
    List<Integer> getKeysFromCountMap(Map<Integer, List<Double>> m) {
        List<Integer> keysList;
        keysList = new ArrayList<>();
        m.entrySet().forEach((e) -> {
            keysList.add(e.getKey());
        });
        return keysList;
    }

    /**
     * Returns the highest reading form a map of readings and respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return Double
     */
    Double getHigh(Map<Double, Integer> m) {
        // Sort map
        if (!(m instanceof TreeMap)) {
            m = sortMap(m);
        }

        List<Double> keysList = getKeysFromValueMap(m);

        // Return the last value in the sorted list which is sorted by default
        // from low to high
        return keysList.get(keysList.size() - 1);
    }

    /**
     * Returns the average of all readings in a map of weather data readings and
     * their respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return Double
     */
    Double getAverage(Map<Double, Integer> m) {
        Double sum = 0.0;
        Integer count = 0;
        for (Map.Entry<Double, Integer> e : m.entrySet()) {
            sum += e.getKey() * (double) e.getValue();
            count += e.getValue();
        }

        return sum / (double) count;
    }

    /**
     * Returns the median of all readings in a map of weather data readings and
     * their respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return Double
     */
    Double getMedian(Map<Double, Integer> m) {
        Double median;

        if (!(m instanceof TreeMap)) {
            m = sortMap(m);
        }

        List<Double> keysList = getKeysFromValueMap(m);

        Integer listLength = keysList.size();
        Integer listMid = listLength / 2;

        if (listLength % 2 == 0) {
            // median is the average of the middle two values of the list
            median = (keysList.get(listMid - 1) + keysList.get(listMid)) / 2.0;
        } else {
            // median is the middle value in the list
            median = keysList.get(listMid);
        }
        return median;
    }

    /**
     * Returns a list of the most frequent readings from a map of weather data
     * readings and their respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return List<Double>
     */
    List<Double> getMostFrequent(Map<Double, Integer> m) {
        Map<Integer, List<Double>> countMap = new TreeMap<>();
        m.entrySet().forEach((Map.Entry<Double, Integer> e) -> {
            if (countMap.containsKey(e.getValue())) {
                // key exists so list exists
                List<Double> l;
                l = countMap.get(e.getValue());
                l.add(e.getKey());
            } else {
                // New entry. Create the list to store all readings with the
                // same count
                List<Double> l;
                l = new ArrayList();
                l.add(e.getKey());
                countMap.put(e.getValue(), l);
            }
        });

        List<Integer> keysList;
        keysList = getKeysFromCountMap(countMap);

        return countMap.get(keysList.get(keysList.size() - 1));
    }

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        EnvData evalMain = new EnvData();

        // Data stores to hold the values read from the data file
        Map<Double, Integer> windSpeedMap = new TreeMap<>();
        Map<Double, Integer> airTempMap = new TreeMap<>();
        Map<Double, Integer> barPressureMap = new TreeMap<>();

        // Check for file name in command line
        if (args.length == 0) {
            System.out.println("Please provide name of the data file as a command line argument");
            System.exit(0);
        }

        System.out.println("Evaluating data file: " + args[0]);

        Scanner scanner = null;

        try {
            // Provide a BufferedReader for the data file to Scanner
            scanner = new Scanner(new BufferedReader(new FileReader(args[0])));

            while (scanner.hasNext()) {
                // Create a scanner to scan the record line
                Scanner lineScanner = new Scanner(scanner.nextLine());
                
                int col = 0;
                while (lineScanner.hasNext()) {
                    col++;
                    Integer count;
                    if (lineScanner.hasNextDouble()) {
                        double value = lineScanner.nextDouble();
                        switch (col) {
                            case 3:
                                count = airTempMap.get(value);
                                airTempMap.put(value, (count == null) ? 1 : count + 1);
                                break;
                            case 4:
                                count = barPressureMap.get(value);
                                barPressureMap.put(value, (count == null) ? 1 : count + 1);
                                break;
                            case 9:
                                count = windSpeedMap.get(value);
                                windSpeedMap.put(value, (count == null) ? 1 : count + 1);
                                break;
                            default:
                        }
                    } else {
                        lineScanner.next();
                    }
                }
            }

            System.out.println("Total readings: " + evalMain.getKeysFromValueMap(windSpeedMap).size());
            System.out.printf("Air temperature: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n",
                    evalMain.getAverage(airTempMap),
                    evalMain.getMedian(airTempMap),
                    evalMain.getHigh(airTempMap),
                    evalMain.getMostFrequent(airTempMap).toString(),
                    airTempMap.get(evalMain.getMostFrequent(airTempMap).toArray()[0]));
            System.out.printf("Barometric pressure: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n",
                    evalMain.getAverage(barPressureMap),
                    evalMain.getMedian(barPressureMap),
                    evalMain.getHigh(barPressureMap),
                    evalMain.getMostFrequent(barPressureMap).toString(),
                    barPressureMap.get(evalMain.getMostFrequent(barPressureMap).toArray()[0]));
            System.out.printf("Wind speed: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n\n",
                    evalMain.getAverage(windSpeedMap),
                    evalMain.getMedian(windSpeedMap),
                    evalMain.getHigh(windSpeedMap),
                    evalMain.getMostFrequent(windSpeedMap).toString(),
                    windSpeedMap.get(evalMain.getMostFrequent(windSpeedMap).toArray()[0]));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnvData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != scanner) {
                scanner.close();
            }
        }
    }
}
