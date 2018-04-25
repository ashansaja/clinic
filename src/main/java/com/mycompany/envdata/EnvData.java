/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.envdata;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ashwinrao
 */
public class EnvData {

    Map<Double, Integer> sortMap(Map<Double, Integer> m) {
        // Convert to TreeMap to get sorted map
        if (!(m instanceof TreeMap)) {
            m = new TreeMap<>(m);
        }
        return m;
    }

    List getKeysFromValueMap(Map<Double, Integer> m) {
        // Extract keys into a list
        List keysList = new ArrayList();
        for (Map.Entry<Double, Integer> e : m.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                keysList.add(e.getKey());
            }
        }
        return keysList;
    }

    List getKeysFromCountMap(Map<Integer, List> m) {
        List keysList = new ArrayList();
        for (Map.Entry<Integer, List> e : m.entrySet()) {
            keysList.add(e.getKey());
        }
        return keysList;
    }

    Double getHigh(Map<Double, Integer> m) {
        // Sort map
        m = sortMap(m);

        List keysList = getKeysFromValueMap(m);
        return (double) keysList.get(keysList.size() - 1);
    }

    Double getAverage(Map<Double, Integer> m) {
        Double sum = 0.0;
        Integer count = 0;
        for (Map.Entry<Double, Integer> e : m.entrySet()) {
            sum += e.getKey() * (double) e.getValue();
            count += e.getValue();
        }

        return sum / (double) count;
    }

    Double getMedian(Map<Double, Integer> m) {
        Double median;

        m = sortMap(m);

        List keysList = getKeysFromValueMap(m);

        Integer listLength = keysList.size();
        Integer listMid = listLength / 2;

        if (listLength % 2 == 0) {
            median = ((Double) keysList.get(listMid - 1) + (Double) keysList.get(listMid)) / 2.0;
        } else {
            median = (Double) keysList.get(listMid);
        }
        return median;
    }

    List getMostFrequent(Map<Double, Integer> m) {
        Map<Integer, List> countMap = new TreeMap<>();
        for (Map.Entry<Double, Integer> e : m.entrySet()) {
            if (countMap.containsKey(e.getValue())) {
                // key exist so list exists
                List l = countMap.get(e.getValue());
                l.add(e.getKey());
            } else {
                List l = new ArrayList();
                l.add(e.getKey());
                countMap.put(e.getValue(), l);
            }
        }

        List keysList = getKeysFromCountMap(countMap);
        
        return countMap.get((Integer)keysList.get(keysList.size()-1));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EnvData evalMain = new EnvData();

        Map<Double, Integer> windSpeedMap = new HashMap<>();
        Map<Double, Integer> airTempMap = new HashMap<>();
        Map<Double, Integer> barPressureMap = new HashMap<>();

        // Check for file name in command line
        if (args.length == 0) {
            System.out.println("Please provide name of the data file as a command line argument");
            System.exit(0);
        }

        System.out.println("Evaluating data file: " + args[0]);

        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(args[0])));
            
            while (scanner.hasNext()) {
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
