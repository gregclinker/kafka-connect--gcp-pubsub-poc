echo $KAFKACONNECT
echo ""
echo ""
echo "delete"
curl -s -X DELETE "http://$KAFKACONNECT:28083/connectors/kafka-connect-gcp-pubsub-poc"
echo ""
echo ""
echo "create"
curl -s -X POST  -H "Content-Type: application/json" "http://$KAFKACONNECT:28083/connectors" --data @test-connector.json
echo ""
echo ""
echo "list"
curl -s -X GET "http://$KAFKACONNECT:28083/connectors/kafka-connect-gcp-pubsub-poc" | jq .
