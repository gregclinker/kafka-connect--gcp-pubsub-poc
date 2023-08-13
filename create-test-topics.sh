#
echo "/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=${KAFKA}:29092 --create --if-not-exists --topic pubsub-topic --partitions 1 --replication-factor 1"
#
/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=${KAFKA}:29092 --create --if-not-exists --topic pubsub-topic --partitions 1 --replication-factor 1
