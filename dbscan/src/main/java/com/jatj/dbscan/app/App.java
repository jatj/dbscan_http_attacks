package com.jatj.dbscan.app;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * DBScan app, will load flows from dataset and execute DBSCan on them 
 * trying to find anomalies (http dos attacks).
 */
public class App {
    private static final Logger logger = Logger.getLogger("DBScan_app");

    public static void main( String[] args ) {
        logger.log(Level.INFO, "DBScan running...");
    }
}
