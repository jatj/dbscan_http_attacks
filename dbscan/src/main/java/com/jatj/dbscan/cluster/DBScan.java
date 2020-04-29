package com.jatj.dbscan.cluster;

import com.jatj.dbscan.cluster.structures.Cluster;
import com.jatj.dbscan.cluster.structures.DataPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBScan implements ClusterAlgorithm{
	
	public List<DataPoint> points;
    public List<Cluster> clusters;
	
	public double maxDistance;
	public int minPoints;
	
	public DBScan(double maxDistance, int minPoints) {
		this.points = new ArrayList();
		this.clusters = new ArrayList();
		this.maxDistance = maxDistance;
		this.minPoints = minPoints;
	}

	public void cluster() {
		int n = 0;
		for(DataPoint p : points) {
			
			if(!p.isVisited()) {
				p.visit();
				List<DataPoint> neighbors = p.getNeighbors(points, maxDistance);
				
				if(neighbors.size() >= minPoints) {
					Cluster c = new Cluster(clusters.size());
					buildCluster(p,c,neighbors);
					clusters.add(c);
				}
			}
		}
	}

	private void buildCluster(DataPoint p, Cluster c, List<DataPoint> neighbors) {
		c.addPoint(p);
		while(neighbors.size() > 0){
			DataPoint neighbor = neighbors.remove(0);
			if(!neighbor.isVisited()) {
				neighbor.visit();
				List<DataPoint> neighborNeighbors = neighbor.getNeighbors(points, maxDistance);
				if(neighborNeighbors.size() >= minPoints) {
					neighbors.addAll(neighborNeighbors);
				}
			}
			if(neighbor.getCluster() == -1) {
				c.addPoint(neighbor);
			}
		}
	}

	public void setPoints(List<DataPoint> points) {
		this.points = points;
	}
	
}