package com.jatj.dbscan.flow;

import com.jatj.dbscan.flow.keys.FlowKey;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * FlowData, represents the relevant features of a flow
 */
public class FlowData {
    // Map index in csv string to the feature name.
    // srcip,srcport,dstip,dstport,proto,total_fpackets,total_fvolume,total_bpackets,total_bvolume,min_fpktl,mean_fpktl,max_fpktl,std_fpktl,min_bpktl,mean_bpktl,max_bpktl,std_bpktl,min_fiat,mean_fiat,max_fiat,std_fiat,min_biat,mean_biat,max_biat,std_biat,duration,min_active,mean_active,max_active,std_active,min_idle,mean_idle,max_idle,std_idle,sflow_fpackets,sflow_fbytes,sflow_bpackets,sflow_bbytes,fpsh_cnt,bpsh_cnt,furg_cnt,burg_cnt,total_fhlen,total_bhlen,dscp,firstTime,flast,blast,class
    private static final Map<Integer, String> INDEX_TO_FEATURE = new HashMap<Integer, String>() {
        {
            put(0, "srcip");
            put(1, "srcport");
            put(2, "dstip");
            put(3, "dstport");
            put(4, "proto");
            put(5, "total_fpackets");
            put(6, "total_fvolume");
            put(7, "total_bpackets");
            put(8, "total_bvolume");
            put(9, "fpktl"); // min
            put(10, "fpktl"); // mean
            put(11, "fpktl"); // max
            put(12, "fpktl"); // std
            put(13, "bpktl"); // min
            put(14, "bpktl"); // mean
            put(15, "bpktl"); // max
            put(16, "bpktl"); // std
            put(17, "fiat"); // min
            put(18, "fiat"); // mean
            put(19, "fiat"); // max
            put(20, "fiat"); // std
            put(21, "biat"); // min
            put(22, "biat"); // mean
            put(23, "biat"); // max
            put(24, "biat"); // std
            put(25, "duration");
            put(26, "active"); // min
            put(27, "active"); // mean
            put(28, "active"); // max
            put(29, "active"); // std
            put(30, "idle"); // min
            put(31, "idle"); // mean
            put(32, "idle"); // max
            put(33, "idle"); // std
            put(34, "sflow_fpackets");
            put(35, "sflow_fbytes");
            put(36, "sflow_bpackets");
            put(37, "sflow_bbytes");
            put(38, "fpsh_cnt");
            put(39, "bpsh_cnt");
            put(40, "furg_cnt");
            put(41, "burg_cnt");
            put(42, "total_fhlen");
            put(43, "total_bhlen");
            put(44, "dscp");
            put(45, "firstTime");
            put(46, "flast");
            put(47, "blast");
            put(48, "class");
        }
    };
    // Map from feature name to index in flow features array
    private static final Map<String, Integer> FLOW_FEATURE_TO_INDEX = new HashMap<String, Integer>() {
        {
            put("total_fpackets", TOTAL_FPACKETS);
            put("total_fvolume", TOTAL_FVOLUME);
            put("total_bpackets", TOTAL_BPACKETS);
            put("total_bvolume", TOTAL_BVOLUME);
            put("fpktl", FPKTL);
            put("bpktl", BPKTL);
            put("fiat", FIAT);
            put("biat", BIAT);
            put("duration", DURATION);
            put("active", ACTIVE);
            put("idle", IDLE);
            put("sflow_fpackets", SFLOW_FPACKETS);
            put("sflow_fbytes", SFLOW_FBYTES);
            put("sflow_bpackets", SFLOW_BPACKETS);
            put("sflow_bbytes", SFLOW_BBYTES);
            put("fpsh_cnt", FPSH_CNT);
            put("bpsh_cnt", BPSH_CNT);
            put("furg_cnt", FURG_CNT);
            put("burg_cnt", BURG_CNT);
            put("total_fhlen", TOTAL_FHLEN);
            put("total_bhlen", TOTAL_BHLEN);
        }
    };

    private static Logger log = Logger.getLogger(FlowData.class.getSimpleName());

    /**
     * Constants
     */
    static final int IP_TCP = 6;
    static final int IP_UDP = 17;

    static final int P_FORWARD = 0;
    static final int P_BACKWARD = 1;

    static final int ADD_SUCCESS = 0;
    static final int ADD_CLOSED = 1;
    static final int ADD_IDLE = 2;

    /**
     * Configurables
     */
    static final int FLOW_TIMEOUT = 600000000;
    static final int IDLE_THRESHOLD = 1000000;

    /**
     * Features indexes
     */
    static final int TOTAL_FPACKETS = 0;
    static final int TOTAL_FVOLUME = 1;
    static final int TOTAL_BPACKETS = 2;
    static final int TOTAL_BVOLUME = 3;
    static final int FPKTL = 4;
    static final int BPKTL = 5;
    static final int FIAT = 6;
    static final int BIAT = 7;
    static final int DURATION = 8;
    static final int ACTIVE = 9;
    static final int IDLE = 10;
    static final int SFLOW_FPACKETS = 11;
    static final int SFLOW_FBYTES = 12;
    static final int SFLOW_BPACKETS = 13;
    static final int SFLOW_BBYTES = 14;
    static final int FPSH_CNT = 15;
    static final int BPSH_CNT = 16;
    static final int FURG_CNT = 17;
    static final int BURG_CNT = 18;
    static final int TOTAL_FHLEN = 19;
    static final int TOTAL_BHLEN = 20;
    static final int NUM_FEATURES = 21;

    /**
     * Properties
     */
    public IFlowFeature[] f; // A map of the features to be exported
    public boolean valid; // Has the flow met the requirements of a bi-directional flow
    public long activeStart; // The starting time of the latest activity
    public long firstTime; // The time of the first packet in the flow
    public long flast; // The time of the last packet in the forward direction
    public long blast; // The time of the last packet in the backward direction
    public short pdir; // Direction of the current packet
    public int srcip; // IP address of the source (client)
    public int srcport; // Port number of the source connection
    public int dstip; // IP address of the destination (server)
    public int dstport; // Port number of the destionation connection.
    public byte proto; // The IP protocol being used for the connection.
    public byte dscp; // The first set DSCP field for the flow.
    public FlowKey forwardKey;
    public FlowKey backwadKey;
    public String flowClass;

    public FlowData(int srcip, int srcport, int dstip, int dstport, byte proto, byte dscp, long firstTime, long flast, long blast, IFlowFeature[] f, String flowClass) {
        this.forwardKey = new FlowKey(srcip, srcport, dstip, dstport, proto);
        this.backwadKey = new FlowKey(dstip, dstport, srcip, srcport, proto);
        this.f = f;
        this.valid = false;
        this.srcip = srcip;
        this.srcport = srcport;
        this.dstip = dstip;
        this.dstport = dstport;
        this.proto = proto;
        this.dscp = dscp;
        this.firstTime = firstTime;
        this.flast = flast;
        this.blast = blast;
        this.flowClass = flowClass;
    }

    @Override   
    public boolean equals(Object obj) {
        if (!(obj instanceof FlowData))
            return false;
        FlowData ref = (FlowData) obj;
        return this.forwardKey.equals(ref.forwardKey) && this.backwadKey.equals(ref.backwadKey);
    }

    public static FlowData getFlowData(String str){
        String[] tokens = str.split(",");
        IFlowFeature[] f = new IFlowFeature[NUM_FEATURES];
        String flowClass = "UNKNOWN";
        long firstTime = 0; // The time of the first packet in the flow
        long flast = 0; // The time of the last packet in the forward direction
        long blast = 0; // The time of the last packet in the backward direction
        int srcip = 0; // IP address of the source (client)
        int srcport = 0; // Port number of the source connection
        int dstip = 0; // IP address of the destination (server)
        int dstport = 0; // Port number of the destionation connection.
        byte proto = 0; // The IP protocol being used for the connection.
        byte dscp = 0; // The first set DSCP field for the flow.

        f[TOTAL_FPACKETS] = new ValueFlowFeature(0);
        f[TOTAL_FVOLUME] = new ValueFlowFeature(0);
        f[TOTAL_BPACKETS] = new ValueFlowFeature(0);
        f[TOTAL_BVOLUME] = new ValueFlowFeature(0);
        f[FPKTL] = new DistributionFlowFeature(0);
        f[BPKTL] = new DistributionFlowFeature(0);
        f[FIAT] = new DistributionFlowFeature(0);
        f[BIAT] = new DistributionFlowFeature(0);
        f[DURATION] = new ValueFlowFeature(0);
        f[ACTIVE] = new DistributionFlowFeature(0);
        f[IDLE] = new DistributionFlowFeature(0);
        f[SFLOW_FPACKETS] = new ValueFlowFeature(0);
        f[SFLOW_FBYTES] = new ValueFlowFeature(0);
        f[SFLOW_BPACKETS] = new ValueFlowFeature(0);
        f[SFLOW_BBYTES] = new ValueFlowFeature(0);
        f[FPSH_CNT] = new ValueFlowFeature(0);
        f[BPSH_CNT] = new ValueFlowFeature(0);
        f[FURG_CNT] = new ValueFlowFeature(0);
        f[BURG_CNT] = new ValueFlowFeature(0);
        f[TOTAL_FHLEN] = new ValueFlowFeature(0);
        f[TOTAL_BHLEN] = new ValueFlowFeature(0);

        for(int i = 0; i<tokens.length; i++) {
            String token = tokens[i];
            String featureName = INDEX_TO_FEATURE.get(i);
            switch(featureName){
                case "class":
                    flowClass = token;
                break;
                case "srcip":
                    srcip = Integer.parseInt(token);
                break;
                case "srcport":
                    srcport = Integer.parseInt(token);
                break;
                case "dstip":
                    dstip = Integer.parseInt(token);
                break;
                case "dstport":
                    dstport = Integer.parseInt(token);
                break;
                case "proto":
                    proto = (byte) Integer.parseInt(token);
                break;
                case "dscp":
                    dscp = (byte) Integer.parseInt(token);
                break;
                case "firstTime":
                    firstTime = Long.parseLong(token);
                break;
                case "flast":
                    flast = Long.parseLong(token);
                break;
                case "blast":
                    blast = Long.parseLong(token);
                break;
                default:
                    if(FLOW_FEATURE_TO_INDEX.containsKey(featureName)){
                        // Append to features array
                        Integer index = FLOW_FEATURE_TO_INDEX.get(featureName);
                        if(f[index] instanceof DistributionFlowFeature){
                            ((DistributionFlowFeature) f[index]).load(token);
                        } else {
                            f[index].Set(Long.parseLong(token));
                        }
                    } else {
                        log.log(Level.WARNING, "Ignoring feature: %s", featureName);
                    }
                break;
            }
        }
        return new FlowData(srcip, srcport, dstip, dstport, proto, dscp, firstTime, flast, blast, f, flowClass);
    }
    
}