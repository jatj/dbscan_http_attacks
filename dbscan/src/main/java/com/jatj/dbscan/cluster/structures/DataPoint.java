package com.jatj.dbscan.cluster.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class DataPoint {
	private boolean isVisited = false;
	private UUID uuid;
	private ArrayList<DataPoint> neighbors;

	public DataPoint(){
		uuid = UUID.randomUUID();
	}
 
	public double distance(DataPoint datapoint){
		return Double.MAX_VALUE;
	}
	
	public void setCluster(int id){}
	
	public int getCluster(){
		return -1;
	}

	public void visit(){
		isVisited = true;
	}

	public boolean isVisited(){
		return isVisited;
	}

	public String getLabel(){
		return "undefined";
	}

	public boolean isDataPoint(DataPoint p){
		return this.uuid.equals(p.uuid);
	}

	public String getUuid(){
		return uuid.toString();
	}

	public List<DataPoint> getNeighbors(List<DataPoint> points, double maxDistance) {
		if(neighbors == null){
			calculateNeighbors(points, maxDistance);
		}
		return neighbors;
	}

	private void calculateNeighbors(List<DataPoint> points, double maxDistance){
		neighbors = new ArrayList<>();
		for (DataPoint p : points) {
			if(this.isDataPoint(p)) continue;
			
			if(distance(p) <= maxDistance) {
				neighbors.add(p);
			}
		}
	}
}