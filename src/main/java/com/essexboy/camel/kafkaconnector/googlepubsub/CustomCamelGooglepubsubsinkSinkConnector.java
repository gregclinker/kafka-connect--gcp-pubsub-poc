package com.essexboy.camel.kafkaconnector.googlepubsub;

import org.apache.camel.kafkaconnector.googlepubsubsink.CamelGooglepubsubsinkSinkConnector;
import org.apache.kafka.connect.connector.Task;

public class CustomCamelGooglepubsubsinkSinkConnector extends CamelGooglepubsubsinkSinkConnector {
    @Override
    public Class<? extends Task> taskClass() {
        return CustomCamelGooglepubsubsinkSinkTask.class;
    }
}