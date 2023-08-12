package com.essexboy.camel.kafkaconnector.googlepubsub;

import org.apache.camel.kafkaconnector.googlepubsubsink.CamelGooglepubsubsinkSinkTask;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class CustomCamelGooglepubsubsinkSinkTask extends CamelGooglepubsubsinkSinkTask {

    private static final Logger LOG = LoggerFactory.getLogger(CustomCamelGooglepubsubsinkSinkTask.class);

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        sinkRecords.forEach(r -> {
            LOG.info(toString(r));
        });
        super.put(sinkRecords);
    }

    // don't print the value
    public String toString(SinkRecord sinkRecord) {
        return "SinkRecord{kafkaOffset=" + sinkRecord.kafkaOffset()
                + ", timestamp=" + sinkRecord.timestamp()
                + ", timestampType=" + sinkRecord.timestampType()
                + ", topic='" + sinkRecord.topic()
                + ", kafkaPartition=" + sinkRecord.kafkaPartition()
                + ", key=" + sinkRecord.key()
                + ", keySchema=" + sinkRecord.keySchema()
                + ", valueSchema=" + sinkRecord.valueSchema()
                + ", headers=" + sinkRecord.headers() + '}';
    }
}