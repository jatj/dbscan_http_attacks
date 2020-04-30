package com.jatj.dbscan.cluster.structures;

import java.util.ArrayList;
import java.util.List;
 
public class Cluster {
	
	public List<DataPoint> points;
	public DataPoint centroid;
	public int id;
	
	public Cluster(final int id) {
		this.id = id;
		this.points = new ArrayList();
		this.centroid = null;
	}

	public List getPoints() {
		return points;
	}

	public void addPoint(final DataPoint point) {
		points.add(point);
		point.setCluster(id);
	}

	public void setPoints(final List points) {
		this.points = points;
	}

	public DataPoint getCentroid() {
		return centroid;
	}

	public void setCentroid(final DataPoint centroid) {
		this.centroid = centroid;
	}

	public int getId() {
		return id;
	}

	public void clear() {
		points.clear();
	}
 
}