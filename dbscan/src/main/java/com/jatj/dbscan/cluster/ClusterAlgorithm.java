package com.jatj.dbscan.cluster;

import com.jatj.dbscan.cluster.structures.DataPoint;

import java.util.List;

public interface ClusterAlgorithm {

public void setPoints(List<DataPoint> points);

public void cluster();

}