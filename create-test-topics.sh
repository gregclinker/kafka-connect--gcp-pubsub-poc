#
BOOTSTRAP_SERVER=${KAFKA}:29092
#
echo $BOOTSTRAP_SERVER
#
/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=${BOOTSTRAP_SERVER} --create --if-not-exists --topic pubsub-topic --partitions 1 --replication-factor 1
