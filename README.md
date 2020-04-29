# DBScan for HTTP DDoS attacks

This repo contains the implementation of a DBScan algorithm for clustering network flows and detect anomalies that could be possible ddos attacks.

In this repo we used a HTTP DDoS attack dataset to test the clustering, you can check more of this dataset [here](https://www.unb.ca/cic/datasets/dos-dataset.html) and download it [here](http://205.174.165.80/CICDataset/ISCX-SlowDoS-2016/Dataset/). It was converted from pcap to flows using [flowtbag](https://github.com/DanielArndt/flowtbag). If you have questions on how to prepare the dataset check our pcap parser [repo](https://github.com/jatj/pcapParser).

# Used features:
To calculate the distance between two flows we restricted the features used to the following:
- total_fpackets: Total packets in the forward direction
- total_fvolume: Total bytes in the forward direction
- min_fpktl: The size of the smallest packet sent in the forward direction (in bytes)
- mean_fpktl: The mean size of packets sent in the forward direction (in bytes)
- max_fpktl: The size of the largest packet sent in the forward direction (in bytes)
- std_fpktl: The standard deviation from the mean of the packets sent in the forward direction (in bytes)
- min_fiat: The minimum amount of time between two packets sent in the forward direction (in microseconds)
- mean_fiat: The mean amount of time between two packets sent in the forward direction (in microseconds)
- max_fiat: The maximum amount of time between two packets sent in the forward direction (in microseconds)
- std_fiat: The standard deviation from the mean amount of time between two packets sent in the forward direction (in microseconds)
- min_active: The minimum amount of time that the flow was active before going idle (in microseconds)
- mean_active: The mean amount of time that the flow was active before going idle (in microseconds)
- max_active: The maximum amount of time that the flow was active before going idle (in microseconds)
- std_active: The standard deviation from the mean amount of time that the flow was active before going idle (in microseconds)
- min_idle: The minimum time a flow was idle before becoming active (in microseconds)
- mean_idle: The mean time a flow was idle before becoming active (in microseconds)
- max_idle: The maximum time a flow was idle before becoming active (in microseconds)
- std_idle: The standard devation from the mean time a flow was idle before becoming active (in microseconds)
- sflow_fpackets: The average number of packets in a sub flow in the forward direction
- sflow_fbytes: The average number of bytes in a sub flow in the forward direction
- fpsh_cnt: The number of times the PSH flag was set in packets travelling in the forward direction (0 for UDP)
- furg_cnt: The number of times the URG flag was set in packets travelling in the forward direction (0 for UDP)
- total_fhlen: The total bytes used for headers in the forward direction.

## Running dbscan app

### Running jar
- `mvn clean install`, will generate a .jar under the target directory
- Run the compiled .jar `java -jar target/dbscan-1.0-SNAPSHOT.jar`
- If you need to pass arguments just add them after the .jar path, like: `java -jar target/dbscan-1.0-SNAPSHOT.jar arg1 arg2 arg3`