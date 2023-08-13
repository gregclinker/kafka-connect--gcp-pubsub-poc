# kafka-connect-gcp-pubsub-poc

## kafka-connect docker sandbox with custom GCP pub-sub connector

### run kafka & kafka-connect in docker
clean up docker first if you need to
```shell script
$ ./cleanDocker.sh 
Deleted Networks:
kafka-connect-gcp-pubsub-poc1_default

Total reclaimed space: 0B
Total reclaimed space: 0B
3a58f7873762
b3cd238e2d30
8a87d020d893
```
start zookeeper, kafka & kafka-connect using docker compose & grab the IPs of each
```shell script
$ ./up.sh 
Creating zookeeper ... done
Creating kafka     ... done
Creating kafkaconnect ... done
192.168.0.3 kafka
192.168.0.4 kafkaconnect
```
set some environemnt variables for KAFKA & KAFKACONNECT hosts
```shell script
$ . setKafka.sh ; echo $KAFKA ; echo $KAFKACONNECT
192.168.0.3
192.168.0.4
```

### create a test topic
```shell script
$ ./create-test-topics.sh 
192.168.0.3:29092
Created topic pubsub-topic.
```

```shell script
$ ./describe-all-topics.sh 
192.168.0.3:29092
Topic: quickstart-config	TopicId: Ec7Dz_TETByXADjwiPl0Ww	PartitionCount: 1	ReplicationFactor: 1	Configs: cleanup.policy=compact
	Topic: quickstart-config	Partition: 0	Leader: 1001	Replicas: 1001	Isr: 1001
```
### build and install the custom connector
build the jars
```shell script
mvn clean install -DskipTests
```
copy all the jars collated by the maven maven-dependency-plugin to the volume mounted kafka-connect plugins
```shell script
cp target/*.jar kafka-connect-jars/
```  
restart kafka connect
```shell script
docker restart kafkaconnect
```  
install the connector with the kafka-connect API
```shell script
curl -s -X POST  -H "Content-Type: application/json" "http://$KAFKACONNECT:28083/connectors" --data @test-connector.json
```  
inspect the connector
```shell script
$ curl -s -X GET "http://$KAFKACONNECT:28083/connectors/kafka-connect-gcp-pubsub-poc" | jq .
{
  "name": "kafka-connect-gcp-pubsub-poc",
  "config": {
    "connector.class": "com.essexboy.camel.kafkaconnector.googlepubsub.CustomCamelGooglepubsubsinkSinkConnector",
    "tasks.max": "1",
    "topics": "pubsub-topic",
    "camel.kamelet.google-pubsub-sink.projectId": "kafka-k8s-example",
    "name": "kafka-connect-gcp-pubsub-poc",
    "camel.kamelet.google-pubsub-sink.destinationName": "greg-test1",
    "camel.sink.contentLogLevel": "DEBUG",
    "value.converter": "org.apache.kafka.connect.storage.StringConverter",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter"
  },
  "tasks": [
    {
      "connector": "kafka-connect-gcp-pubsub-poc",
      "task": 0
    }
  ],
  "type": "sink"
}
```  
send some test traffic to the topic
```shell script
$ ./produce.sh 
192.168.0.3
10 records sent, 33.222591 records/sec (0.01 MB/sec), 47.20 ms avg latency, 291.00 ms max latency, 20 ms 50th, 291 ms 95th, 291 ms 99th, 291 ms 99.9th.
```  


