package io.github.assets.app.messaging.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka-byte-stream")
public class KafkaByteArrayProperties {
    private Map<String, Object> consumerProps;

    private Map<String, Object> producerProps;

    public Map<String, Object> getConsumerProps() {
        return consumerProps;
    }

    public void setConsumerProps(final Map<String, Object> consumerProps) {
        this.consumerProps = consumerProps;
    }

    public Map<String, Object> getProducerProps() {
        return producerProps;
    }

    public void setProducerProps(final Map<String, Object> producerProps) {
        this.producerProps = producerProps;
    }

    //    public Map<String, String> getConsumerProps() {
//        return consumerProps;
//    }
//
//    public void setConsumerProps(Map<String, String> consumerProps) {
//        this.consumerProps = consumerProps;
//    }
//
//    public Map<String, String> getProducerProps() {
//        return producerProps;
//    }
//
//    public void setProducerProps(Map<String, String> producerProps) {
//        this.producerProps = producerProps;
//    }

    /*public Map<String, Object> getConsumerProps() {
        return (Map) consumerProps;
    }

    public void setConsumer(Map<String, String> consumerProps) {
        this.consumerProps = consumerProps;
    }

    public Map<String, Object> getProducerProps() {
        return (Map) producerProps;
    }

    public void setProducer(Map<String, String> producerProps) {
        this.producerProps = producerProps;
    }*/
}
