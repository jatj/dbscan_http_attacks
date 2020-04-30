package com.jatj.dbscan;

import com.jatj.dbscan.flow.features.IFlowFeature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Helpers functions for DBScan
 */
public class Helpers {

    /**
     * Calculates the standard deviation of a feature.
     * @param sqsum the square sum of the values
     * @param sum the sum of the values
     * @param count the count of the values 
     * @return the standar deviation
     */
    public static float stddev(float sqsum, float sum, long count) {
        if (count < 2) {
            return 0;
        }
        float n = (float) count;
        return (float) Math.sqrt((sqsum - (sum * sum / n)) / (n - 1));
    }

    /**
     * Returns the minimum of two longs
     * @param i1
     * @param i2
     * @return the minimum from i1 and i2
     */
    public static long min(long i1, long i2) {
        if (i1 < i2) {
            return i1;
        }
        return i2;
    }

    /**
     * Returns the mininmum of two ints
     * @param i1
     * @param i2
     * @return the minimum from i1 and i2
     */
    public static int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        }
        return i2;
    }

    /**
     * Get the mode of an array 
     *
     * @param a the array containing integers
     * @return int the value that appears most often in the array
     */
    public static int mode(ArrayList<Integer> a){
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        int maxCount = 0;
        int maxN = -1;
        for(int i = 0; i < a.size(); i++){
            Integer n = a.get(i);
            int count = counts.getOrDefault(n, 0)+1;
            counts.put(n, count);
            if(count > maxCount){
                maxCount = count;
                maxN = n;
            }
        }
        return maxN;
    }

    /** Calculates the normalized squared distance of two values. */
    public static double normalizedSquaredDistance(double a, double b) throws IllegalArgumentException{
        if(a == 0 && b == 0){
            return 0;
        }
        if(a < 0 || b < 0) {
            throw new IllegalArgumentException("Vector values are not positive");
        }
        return Math.pow((a-b)/(a+b),2);
    }

    /** Calculates the normalized euclidean distance between two vectors. */
    public static double normalizedEuclideanDistance(double[] a, double[] b) throws IllegalArgumentException {
        if(a.length != b.length){
            throw new IllegalArgumentException("Vector's size differ");
        }
        double sum = 0;
        for(int i = 0; i < a.length; i++){
            sum+=normalizedSquaredDistance(a[i], b[i]);
        }
        return Math.sqrt(sum);
    }
    
    /** Calculates the squared distance of two values. */
    public static double squaredDistance(double a, double b) throws IllegalArgumentException{
        if(a == 0 && b == 0){
            return 0;
        }
        if(a < 0 || b < 0) {
            throw new IllegalArgumentException("Vector values are not positive");
        }
        return Math.pow((a-b),2);
    }

    /** Calculates the euclidean distance between two vectors. */
    public static double euclideanDistance(double[] a, double[] b) throws IllegalArgumentException {
        if(a.length != b.length){
            throw new IllegalArgumentException("Vector's size differ");
        }
        double sum = 0;
        for(int i = 0; i < a.length; i++){
            sum+=squaredDistance(a[i], b[i]);
        }
        return Math.sqrt(sum);
    }

    /** Calculates the manhattan distance between two vectors. */
    public static double manhattanDistance(double[] a, double[] b) throws IllegalArgumentException {
        if(a.length != b.length){
            throw new IllegalArgumentException("Vector's size differ");
        }
        double sum = 0;
        for(int i = 0; i < a.length; i++){
            sum+=Math.abs(a[i] - b[i]);
        }
        return Math.sqrt(sum);
    }

    /** Fills the vector with the feature values from fromIndex. */
    public static double[] fillVector(double[] vector, int fromIndex, IFlowFeature feature) throws IllegalArgumentException {
        ArrayList<Long> vals = feature.ToArrayList();
        if (fromIndex+vals.size() > vector.length){
            throw new IllegalArgumentException("Can't allocate all feature values on vector");
        }
        for(int i = 0; i < vals.size(); i++){
            vector[i] = vals.get(i);
        }
        return vector;
    }

    public static ArrayList<String> getCsv(String resource){
        ArrayList<String> csv = new ArrayList<>();
        try {
            InputStream i = Helpers.class.getClassLoader().getResourceAsStream(resource);
            BufferedReader r = new BufferedReader(new InputStreamReader(i));

            String l;
            while((l = r.readLine()) != null) {
                csv.add(l);
            } 
            i.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csv;
    }

    public static int ipToInt(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        int result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
        
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        
        }
        return result;
    }

}