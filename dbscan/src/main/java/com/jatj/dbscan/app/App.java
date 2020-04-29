package com.jatj.dbscan.app;

import com.jatj.dbscan.Helpers;
import com.jatj.dbscan.flow.FlowData;
import com.jatj.dbscan.cluster.DBScan;
import com.jatj.dbscan.cluster.structures.DataPoint;
import com.jatj.dbscan.cluster.structures.Cluster;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * DBScan app, will load flows from dataset and execute DBSCan on them 
 * trying to find anomalies (http dos attacks).
 */
public class App {
    private static final Logger logger = Logger.getLogger("DBScan_app");
    private static final String RESOURCE_FILE = "dataset_1k.csv";
    private static final double MAX_DISTANCE = 0.05;
    private static final int MIN_POINTS = 5;

    public static void main( String[] args ) {
        App app = new App();

        ArrayList<DataPoint> flows = loadFlows(RESOURCE_FILE);
        logger.log(Level.INFO, String.format("Loaded %d samples", flows.size()));

        DBScan dbscan = new DBScan(MAX_DISTANCE, MIN_POINTS);
        dbscan.setPoints(flows);
        logger.log(Level.INFO, "DBScan running...");
        dbscan.cluster();
        logger.log(Level.INFO, String.format("DBScan finished, found %d clusters", dbscan.clusters.size()));

        for(Cluster c : dbscan.clusters){
            int attacks = 0;
            int normals = 0;
            for(DataPoint p : c.points){
                if(p.getLabel().equals("attack")){
                    attacks++;
                }else{
                    normals++;
                }
            }
            logger.log(Level.INFO, String.format("Cluster(%d) with size %d, attacks(%d), normals(%d)", c.id, c.points.size(), attacks, normals));
        }
    }

    private static ArrayList<DataPoint> loadFlows(String resource){
        ArrayList<String> csv = Helpers.getCsv(resource);
        csv.remove(0); // drop first row which are the field names

        // Load flows
        ArrayList<DataPoint> flows = new ArrayList<>();
        for(String row : csv){
            flows.add((DataPoint) FlowData.getFlowData(row));
        }
        return flows;
    }
}
