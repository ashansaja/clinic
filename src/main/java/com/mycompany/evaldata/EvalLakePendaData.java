/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.evaldata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ashwinrao
 */
public class EvalLakePendaData {
    
    double getAverage(Map<Double, Integer> m) {
        Double sum = 0.0;
        Integer count = 0;
        for (Map.Entry<Double, Integer> e : m.entrySet()) {
            sum += e.getKey() * (double)e.getValue();
            count += e.getValue();
        }
        
        return sum / (double)count;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EvalLakePendaData evalMain = new EvalLakePendaData();
        
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
            scanner = new Scanner(new BufferedReader(new FileReader(args[0])));

            while (scanner.hasNext()) {
                Scanner lineScanner = new Scanner(scanner.nextLine());
                int col = 0;
                while (lineScanner.hasNext()) {
                    col++;
                    Integer count = 0;
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
            
            
            System.out.println("Air temperature: Average " + evalMain.getAverage(airTempMap));
            System.out.println("Barometric pressure: Average " + evalMain.getAverage(barPressureMap));
            System.out.println("Wind speed: Average " + evalMain.getAverage(windSpeedMap));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EvalLakePendaData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != scanner) {
                scanner.close();
            }
        }

    }

}
