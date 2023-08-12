#
BOOTSTRAP_SERVER=${KAFKA}:29092
#
echo $BOOTSTRAP_SERVER
#
/opensource/kafka_2.13-3.1.0/bin/kafka-topics.sh --bootstrap-server=${BOOTSTRAP_SERVER} --describe
