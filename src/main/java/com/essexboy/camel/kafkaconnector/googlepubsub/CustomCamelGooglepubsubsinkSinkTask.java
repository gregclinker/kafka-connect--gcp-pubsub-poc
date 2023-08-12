/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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