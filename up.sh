docker-compose up -d
#
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafka  | awk '{print "export KAFKA="$1}' > setKafka.sh
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafkaconnect  | awk '{print "export KAFKACONNECT="$1}' >> setKafka.sh
#
source ./setKafka.sh
#rm setKafka.sh
#
echo $KAFKA | awk '{print $1,"kafka"}'
echo $KAFKACONNECT | awk '{print $1,"kafkaconnect"}'
