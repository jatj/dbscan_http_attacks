package com.jatj.dbscan.app;

import com.jatj.dbscan.Helpers;
import com.jatj.dbscan.flow.FlowData;
import com.jatj.dbscan.cluster.DBScan;
import com.jatj.dbscan.cluster.structures.DataPoint;
import com.jatj.dbscan.cluster.structures.Cluster;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

/**
 * DBScan app, will load flows from dataset and execute DBSCan on them 
 * trying to find anomalies (http dos attacks).
 */
public class App {
    private static final Logger logger = Logger.getLogger("DBScan_app");
    private static final String RESOURCE_FILE = "dataset_500.csv";
    private static final double MAX_DISTANCE = 0.05;
    private static final int MIN_POINTS = 25;

    private static double globalDurationMean = 0;
    private static double globalDurationVariance = 0;
    private static double globalFPS = 0;

    public static void main( String[] args ) {
        App app = new App();

        ArrayList<DataPoint> flows = loadFlows(RESOURCE_FILE);
        logger.log(Level.INFO, String.format("Loaded %d samples, with μ duration: %f, variance duration: %f, fps: %f", flows.size(), globalDurationMean, globalDurationVariance, globalFPS));

        DBScan dbscan = new DBScan(MAX_DISTANCE, MIN_POINTS);
        dbscan.setPoints(flows);
        logger.log(Level.INFO, "DBScan running...");
        dbscan.cluster();
        logger.log(Level.INFO, String.format("DBScan finished, found %d clusters", dbscan.clusters.size()));

        for(Cluster c : dbscan.clusters){
            int attacks = 0;
            int normals = 0;
            double distanceMean = 0;
            double distanceVariance = 0;
            double[] attackIndicators = new double[]{
                0, // duration microseconds
                0, // |flast-blast|
                0, // fps flows per second
                0, // bpms bytes per microsecond
                0, // unique dsts
                0, // cluster duration
            };
            double firstTimeSecs = Double.MAX_VALUE;
            double lastTimeSecs = Double.MIN_VALUE;
            HashSet<String> uniqueDestinations = new HashSet<>();
            Map<Integer, String> indicatorsLabel = new HashMap<Integer, String>(){
                {
                    put(0, "μ Duration");
                    put(1, "μ Time diff");
                    put(2, "fps");
                    put(3, "bpms");
                    put(4, "# Unique destinations");
                    put(5, "Cluster Duration");
                }
            };
            // TODO(abrahamtorres): Move this to DBScan.analize() and refactor.
            Map<String, Double> distances = new HashMap<>();
            for(int i=0; i < c.points.size(); i++){
                FlowData flowA = (FlowData) c.points.get(i);
                // Count classes
                if(flowA.getLabel().equals("attack")){
                    attacks++;
                }else{
                    normals++;
                }
                // Calculate distances
                for(int j=i+1; j < c.points.size(); j++){
                    FlowData flowB = (FlowData) c.points.get(j);
                    String key = String.format("%s-%s", flowA.getUuid(), flowB.getUuid());
                    distances.put(key, flowA.distance(flowB));
                    distanceMean+=distances.get(key);
                }
                // Compute attack indicators
                double[] indicators = flowA.getAttackIndicators();
                for(int k = 0; k < indicators.length; k++){
                    attackIndicators[k] += indicators[k];
                }
                // For fps
                firstTimeSecs = Math.min(firstTimeSecs, indicators[2]);
                lastTimeSecs = Math.max(lastTimeSecs, indicators[2]+indicators[0]);
                // Unique destinations
                String destination = String.format("%d:%d", flowA.dstip, flowA.dstport);
                if(!uniqueDestinations.contains(destination)){
                    uniqueDestinations.add(destination);
                }
            }
            // Calculate cluster statistics
            distanceMean /= c.points.size();
            for(Double dist : distances.values()){
                distanceVariance += Math.pow(dist - distanceMean ,2);
            }
            distanceVariance /= c.points.size(); // distance variance
            attackIndicators[0] /= c.points.size(); // mean duration
            attackIndicators[1] /= c.points.size(); // mean time diff
            attackIndicators[2] = c.points.size()/Math.abs(lastTimeSecs - firstTimeSecs); // fps
            attackIndicators[3] /= c.points.size(); // bpms
            attackIndicators[4] = uniqueDestinations.size(); // # unique destinations
            attackIndicators[5] = Math.abs(lastTimeSecs - firstTimeSecs); // cluster duration

            // Log cluster description
            String clusterDesc = "";
            for(int k = 0; k < attackIndicators.length; k++){
                clusterDesc += String.format("\t- %s: %f\n", indicatorsLabel.get(k), attackIndicators[k]);
            }
            logger.log(Level.INFO, String.format("\n#### Cluster(%d):\n\t- With size %d, distanceMean %f, distanceVariance %f, stddev %f.\n\t- Contains: attacks(%d), normals(%d)\n%s", c.id, c.points.size(), distanceMean, distanceVariance, Math.sqrt(distanceVariance), attacks, normals, clusterDesc));
        }
    }

    private static ArrayList<DataPoint> loadFlows(String resource){
        ArrayList<String> csv = Helpers.getCsv(resource);
        csv.remove(0); // drop first row which are the field names

        // Load flows
        ArrayList<DataPoint> flows = new ArrayList<>();
        for(String row : csv){
            FlowData flow = FlowData.getFlowData(row);
            flows.add((DataPoint) flow);
            globalDurationMean += flow.f[FlowData.DURATION].Get();
        }
        double firstTimeSecs = Double.MAX_VALUE;
        double lastTimeSecs = Double.MIN_VALUE;

        globalDurationMean /= flows.size();
        for(DataPoint flow : flows){
            globalDurationVariance += Math.pow(((FlowData) flow).f[FlowData.DURATION].Get() - globalDurationMean, 2);
            firstTimeSecs = Math.min(firstTimeSecs, ((FlowData) flow).firstTime);
            lastTimeSecs = Math.max(lastTimeSecs, ((FlowData) flow).firstTime+((FlowData) flow).f[FlowData.DURATION].Get());
        }
        globalDurationVariance /= flows.size();
        globalFPS = flows.size()/Math.abs(lastTimeSecs - firstTimeSecs);
        return flows;
    }
}
