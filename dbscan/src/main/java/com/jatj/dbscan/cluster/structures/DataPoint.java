package com.jatj.dbscan.cluster.structures;

public interface DataPoint {
 
	public double distance(DataPoint datapoint);
	
	public void setCluster(int id);
	
	public int getCluster();
}