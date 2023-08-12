echo $KAFKA
/opensource/kafka_2.13-3.1.0/bin/kafka-producer-perf-test.sh --producer-props bootstrap.servers=$KAFKA:29092 --num-records=1200 --record-size=200 --throughput=-1 --topic=pubsub-topic
