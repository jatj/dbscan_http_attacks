package com.jatj.dbscan;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;

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

}