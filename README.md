# DBScan for HTTP DDoS attacks

This repo contains the implementation of a DBScan algorithm for clustering network flows and detect anomalies that could be possible ddos attacks.

In this repo we used a HTTP DDoS attack dataset to test the clustering, you can check more of this dataset [here](https://www.unb.ca/cic/datasets/dos-dataset.html) and download it [here](http://205.174.165.80/CICDataset/ISCX-SlowDoS-2016/Dataset/). It was converted from pcap to flows using [flowtbag](https://github.com/DanielArndt/flowtbag). If you have questions on how to prepare the dataset check our pcap parser [repo](https://github.com/jatj/pcapParser).

## Running dbscan app
- Compile `mvn clean install`, will generate a .jar under the target directory
- Run the compiled .jar `java -jar target/dbscan-1.0-SNAPSHOT.jar`
