package com.jatj.dbscan.flow;

import com.jatj.dbscan.Helpers;

import java.util.ArrayList;

/**
 * DistributionFlowFeature
 */
public class DistributionFlowFeature implements IFlowFeature {
    /**
     * Properties
     */
    public long sum;
	public long sumsq;
	public long count;
	public long min;
    public long max;

    private int valuesInserted = 0;
    private long mean = 0;
    private long stdDev = 0;

    public DistributionFlowFeature(long l){
        Set(l);
    }

    @Override 
    public void Add(long l) {
        sum += l;
        sumsq += l * l;
        count++;
        if ((l < min) || (min == 0)) {
            min = l;
        }
        if (l > max) {
            max = l;
        }
    };
    
    @Override 
    public String Export() {
        long stdDev = 0;
        long mean = 0;
        if (count > 0) {
            stdDev = (long) Helpers.stddev((float) sumsq, (float) sum, count);
            mean = sum / count;
        }
        return String.format("%d,%d,%d,%d", min, mean, max, stdDev);
    };

    @Override 
    public long Get() {
        return count;
    };

    @Override 
    public void Set(long l) {
        sum = l;
        sumsq = l * l;
        count = l;
        min = l;
        max = l;
    };

    @Override
    public ArrayList<Long> ToArrayList() {
        ArrayList<Long> array = new ArrayList<Long>();
        if(valuesInserted > 0){
            array.add(min);
            array.add(mean);
            array.add(max);
            array.add(stdDev);
            return array;
        }

        long stdDev = 0;
        long mean = 0;
        if (count > 0) {
            stdDev = (long) Helpers.stddev((float) sumsq, (float) sum, count);
            mean = sum / count;
        }
        array.add(min);
        array.add(mean);
        array.add(max);
        array.add(stdDev);
        return array;
    };

    /**
     *  Loads the distribution values from a string in the following order:
     * min, mean, max, std.
     * @param token
     */
    public void load(String token) {
        switch(valuesInserted){
            case 0:
                min = Long.parseLong(token);
            break;
            case 1:
                mean = Long.parseLong(token);
            break;
            case 2:
                max = Long.parseLong(token);
            break;
            case 3:
                stdDev = Long.parseLong(token);
            break;
        }
    }
}