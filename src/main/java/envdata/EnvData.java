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

class ValueCountMap {

    private class CountValueMap {

        // Data store to store counts and their associated list of readings
        private Map<Integer, List<Double>> m;

        /**
         * Constructor
         * 
         * @param m Map
         */
        public CountValueMap(Map<Integer, List<Double>> m) {
            this.m = m;
        }

        public Map<Integer, List<Double>> getMap() {
            return m;
        }

        public void setMap(Map<Integer, List<Double>> m) {
            this.m = m;
        }

        /**
         * Gets the keys from a map of weather data reading counts and their
         * respective list of weather reading readings.
         *
         * @param m Map that stores the reading counts and their respective
         * values as a list
         * @return List<Integer>
         */
        public List<Integer> getKeys() {
            List<Integer> keysList;
            keysList = new ArrayList<>();
            m.entrySet().forEach((e) -> {
                keysList.add(e.getKey());
            });
            return keysList;
        }

    }

    // Data store to store weather readings and their associated counts
    private Map<Double, Integer> m;

    /**
     * Constructor with no parameter
     */
    public ValueCountMap() {
        this.m = new TreeMap<>();
    }
    
    /**
     * Constructor
     * 
     * @param map 
     */
    public ValueCountMap(Map<Double, Integer> map) {
        this.m = map;
    }

    private void sortMap() {
        // Convert to TreeMap to get sorted map
        if (!(m instanceof TreeMap)) {
            m = new TreeMap<>(m);
        }
    }

    /**
     * Getter method
     * 
     * @return 
     */
    public Map<Double, Integer> getMap() {
        return m;
    }

    /**
     * Setter method
     * 
     * @param m Map
     */
    public void setMap(Map<Double, Integer> m) {
        this.m = m;
    }

    /**
     * Gets the keys form a map of weather data reading values and their
     * respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return List<Double>
     */
    public List<Double> getKeys() {
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
     * Returns the highest reading form a map of readings and respective counts.
     *
     * @param m Map that stores the reading values and their respective counts
     * @return Double
     */
    public Double getHigh() {
        // Sort map
        if (!(m instanceof TreeMap)) {
            sortMap();
        }

        List<Double> keysList = getKeys();

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
    public Double getAverage() {
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
    public Double getMedian() {
        Double median;

        if (!(m instanceof TreeMap)) {
            sortMap();
        }

        List<Double> keysList = getKeys();

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
    public List<Double> getMostFrequent() {
        CountValueMap countMap = new CountValueMap(new TreeMap<>());
        m.entrySet().forEach(e -> {
            if (countMap.getMap().containsKey(e.getValue())) {
                // key exists so list exists
                List<Double> l;
                l = countMap.getMap().get(e.getValue());
                l.add(e.getKey());
            } else {
                // New entry. Create the list to store all readings with the
                // same count
                List<Double> l;
                l = new ArrayList();
                l.add(e.getKey());
                countMap.getMap().put(e.getValue(), l);
            }
        });

        List<Integer> keysList;
        keysList = countMap.getKeys();

        return countMap.getMap().get(keysList.get(keysList.size() - 1));
    }

}

public class EnvData {

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        // Data stores to hold the values read from the data file
        ValueCountMap windSpeedMap = new ValueCountMap();
        ValueCountMap airTempMap = new ValueCountMap();
        ValueCountMap barPressureMap = new ValueCountMap();

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
                                count = airTempMap.getMap().get(value);
                                airTempMap.getMap().put(value, (count == null) ? 1 : count + 1);
                                break;
                            case 4:
                                count = barPressureMap.getMap().get(value);
                                barPressureMap.getMap().put(value, (count == null) ? 1 : count + 1);
                                break;
                            case 9:
                                count = windSpeedMap.getMap().get(value);
                                windSpeedMap.getMap().put(value, (count == null) ? 1 : count + 1);
                                break;
                            default:
                        }
                    } else {
                        lineScanner.next();
                    }
                }
            }

            System.out.println("Total readings: " + windSpeedMap.getKeys().size());
            System.out.printf("Air temperature: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n",
                    airTempMap.getAverage(),
                    airTempMap.getMedian(),
                    airTempMap.getHigh(),
                    airTempMap.getMostFrequent().toString(),
                    airTempMap.getMap().get(airTempMap.getMostFrequent().toArray()[0]));
            System.out.printf("Barometric pressure: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n",
                    barPressureMap.getAverage(),
                    barPressureMap.getMedian(),
                    barPressureMap.getHigh(),
                    barPressureMap.getMostFrequent().toString(),
                    barPressureMap.getMap().get(barPressureMap.getMostFrequent().toArray()[0]));
            System.out.printf("Wind speed: Average %.2f, Median %.2f, High %.2f, Most frequent %s, Frequency %d\n\n",
                    windSpeedMap.getAverage(),
                    windSpeedMap.getMedian(),
                    windSpeedMap.getHigh(),
                    windSpeedMap.getMostFrequent().toString(),
                    windSpeedMap.getMap().get(windSpeedMap.getMostFrequent().toArray()[0]));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnvData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != scanner) {
                scanner.close();
            }
        }
    }
}
