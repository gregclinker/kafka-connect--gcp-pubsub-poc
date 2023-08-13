# kafka-connect-gcp-pubsub-poc

## kafka-connect docker sandbox with custom GCP pub-sub connector

### run kafka & kafka-connect in docker
clean up docker first if you need to
```shell script
$ ./cleanDocker.sh 
docker system prune -f ; docker network prune -f ; docker volume prune -f ; docker rm -f -v 377465a71015
050e607c31dd
bbf22eb3471b
Total reclaimed space: 0B
Total reclaimed space: 0B
377465a71015
050e607c31dd
bbf22eb3471b
```
start zookeeper, kafka & kafka-connect using docker compose & grab the IPs of each
```shell script
$ ./up.sh 
docker-compose up -d
Creating zookeeper ... done
Creating kafka     ... done
Creating kafkaconnect ... done
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafka  | awk '{print export KAFKA=}' > setKafka.sh
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafkaconnect  | awk '{print export KAFKACONNECT=}' >> setKafka.sh
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
/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=192.168.0.3:29092 --create --if-not-exists --topic pubsub-topic --partitions 1 --replication-factor 1
Created topic pubsub-topic.
```

```shell script
$ ./describe-all-topics.sh | head
/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=192.168.0.3:29092 --describe
Topic: quickstart-config	TopicId: cNhArKGgRiG7DrLT6WIL6w	PartitionCount: 1	ReplicationFactor: 1	Configs: cleanup.policy=compact
	Topic: quickstart-config	Partition: 0	Leader: 1001	Replicas: 1001	Isr: 1001
Topic: pubsub-topic	TopicId: SSznTOeuRviQJCIYwQI9Jg	PartitionCount: 1	ReplicationFactor: 1	Configs: 
	Topic: pubsub-topic	Partition: 0	Leader: 1001	Replicas: 1001	Isr: 1001
Topic: quickstart-offsets	TopicId: KChPcWu5QLatVWMgAVMX1g	PartitionCount: 25	ReplicationFactor: 1	Configs: cleanup.policy=compact
	Topic: quickstart-offsets	Partition: 0	Leader: 1001	Replicas: 1001	Isr: 1001
	Topic: quickstart-offsets	Partition: 1	Leader: 1001	Replicas: 1001	Isr: 1001
	Topic: quickstart-offsets	Partition: 2	Leader: 1001	Replicas: 1001	Isr: 1001
	Topic: quickstart-offsets	Partition: 3	Leader: 1001	Replicas: 1001	Isr: 1001
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
$ ./setConnector.sh 
create
curl -s -X POST  -H Content-Type: application/json http://192.168.0.4:28083/connectors --data @test-connector.json
{"name":"kafka-connect-gcp-pubsub-poc","config":{"connector.class":"com.essexboy.camel.kafkaconnector.googlepubsub.CustomCamelGooglepubsubsinkSinkConnector","tasks.max":"1","key.converter":"org.apache.kafka.connect.storage.StringConverter","value.converter":"org.apache.kafka.connect.storage.StringConverter","topics":"pubsub-topic","camel.kamelet.google-pubsub-sink.destinationName":"greg-test1","camel.kamelet.google-pubsub-sink.projectId":"kafka-k8s-example","camel.sink.contentLogLevel":"DEBUG","name":"kafka-connect-gcp-pubsub-poc"},"tasks":[],"type":"sink"}

list
curl -s -X GET http://192.168.0.4:28083/connectors/kafka-connect-gcp-pubsub-poc | jq .
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
/opensource/kafka_2.13-3.1.0/bin/kafka-producer-perf-test.sh --producer-props bootstrap.servers=192.168.0.3:29092 --num-records=10 --record-size=200 --throughput=-1 --topic=pubsub-topic
10 records sent, 30.487805 records/sec (0.01 MB/sec), 52.70 ms avg latency, 316.00 ms max latency, 24 ms 50th, 316 ms 95th, 316 ms 99th, 316 ms 99.9th.
```  


