/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.client.api;

import java.util.concurrent.CompletableFuture;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.apache.pulsar.client.impl.RawReaderImpl;

/**
 * Topic reader which receives raw messages (i.e. as they are stored in the managed ledger).
 */
public interface RawReader {
    /**
     * Create a raw reader for a topic.
     */
    public static CompletableFuture<RawReader> create(PulsarClient client, String topic) {
        CompletableFuture<Consumer> future = new CompletableFuture<>();
        RawReader r = new RawReaderImpl((PulsarClientImpl)client, topic, future);
        return future.thenCompose((consumer) -> r.seekAsync(MessageId.earliest)).thenApply((ignore) -> r);
    }

    /**
     * Seek to a location in the topic. After the seek, the first message read will be the one with
     * with the specified message ID.
     * @param messageId the message ID to seek to
     */
    CompletableFuture<Void> seekAsync(MessageId messageId);

    /**
     * Read the next raw message for the topic.
     * @return a completable future which will return the next RawMessage in the topic.
     */
    CompletableFuture<RawMessage> readNextAsync();

    /**
     * Close the raw reader.
     */
    CompletableFuture<Void> closeAsync();
}