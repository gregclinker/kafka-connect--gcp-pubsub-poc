echo "docker-compose up -d"
docker-compose up -d
#
echo "docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafka  | awk '{print "export KAFKA="$1}' > setKafka.sh"
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafka  | awk '{print "export KAFKA="$1}' > setKafka.sh
echo "docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafkaconnect  | awk '{print "export KAFKACONNECT="$1}' >> setKafka.sh"
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafkaconnect  | awk '{print "export KAFKACONNECT="$1}' >> setKafka.sh
#
echo $KAFKA | awk '{print $1,"kafka"}'
echo $KAFKACONNECT | awk '{print $1,"kafkaconnect"}'
