package io.github.assets.app.messaging.core;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.function.Consumer;

/**
 * Consumer function for Consumer-records
 * @param <T>
 */
public interface RecordReader<T> extends Consumer<ConsumerRecord<T, T>> {

    /**
     * Performs this operation on the given argument.
     *
     * @param consumerRecord the input argument
     */
    @Override
    void accept(ConsumerRecord<T, T> consumerRecord);
}
