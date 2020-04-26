package com.jatj.dbscan.cluster;

import com.jatj.dbscan.cluster.structures.Cluster;
import com.jatj.dbscan.cluster.structures.DataPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBScan implements ClusterAlgorithm{
	
	public List<DataPoint> points;
    private List<Cluster> clusters;
	
	public double max_distance;
	public int min_points;
	
	public boolean[] visited;
	
	public DBScan(double max_distance, int min_points) {
		this.points = new ArrayList();
		this.clusters = new ArrayList();
		this.max_distance = max_distance;
		this.min_points = min_points;
	}

	public void cluster() {
		Iterator<DataPoint> it = points.iterator();
		int n = 0;
		
		while(it.hasNext()) {
			
			if(!visited[n]) {
				DataPoint d = it.next();
				visited[n] = true;
				List neighbors = getNeighbors(d);
				
				if(neighbors.size() >= min_points) {
					Cluster c = new Cluster(clusters.size());
					buildCluster(d,c,neighbors);
					clusters.add(c);
				}
			}
		}
	}

	private void buildCluster(DataPoint d, Cluster c, List<Integer> neighbors) {
		c.addPoint(d);
		
		for (Integer point : neighbors) {
			DataPoint p = points.get(point);
			if(!visited[point]) {
				visited[point] = true;
				List newNeighbors = getNeighbors(p);
				if(newNeighbors.size() >= min_points) {
					neighbors.addAll(newNeighbors);
				}
			}
			if(p.getCluster() == -1) {
				c.addPoint(p);
			}
		}
	}

	private List getNeighbors(DataPoint d) {
		List neighbors = new ArrayList();
		int i = 0;
		for (DataPoint point : points) {
			double distance = d.distance(point);
			
			if(distance <= max_distance) {
				neighbors.add(i);
			}
			i++;
		}
		
		return neighbors;
	}

	public void setPoints(List<DataPoint> points) {
		this.points = points;
		this.visited = new boolean[points.size()];
	}
	
}