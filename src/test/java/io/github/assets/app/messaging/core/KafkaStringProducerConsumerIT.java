package io.github.assets.app.messaging.core;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.KafkaContainer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.github.assets.app.AppConstants.GENERAL_KAFKA_STRING_TOPIC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This testing is done from the context that the Kafka-string-producer could work just as well from the fixed-asset-service-kafka-resource
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class KafkaStringProducerConsumerIT {

    private List<String> consumedRecords;

    private MockMvc restMockMvc;

    private static boolean started = false;

    private static KafkaContainer kafkaContainer;

    private static String message = "custom string message test";

    @Autowired
    private AppProducer<String> kafkaStringProducer;

    @Autowired
    private ReadableConsumer<String> kafkaStringConsumer;

    private static final int MAX_ATTEMPT = 5;

    @BeforeAll
    public static void startServer() {
        if (!started) {
            startTestcontainer();
            started = true;
        }
    }

    private static void startTestcontainer() {
        kafkaContainer = new KafkaContainer("5.3.1");
        kafkaContainer.start();
        System.setProperty("kafkaBootstrapServers", kafkaContainer.getBootstrapServers());
    }

    @BeforeEach
    public void setup() {
        FixedAssetKafkaTestResource kafkaResource = new FixedAssetKafkaTestResource(kafkaStringProducer);

        this.restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource).build();

        kafkaStringProducer.init();

        consumedRecords = new LinkedList<>();

        kafkaStringConsumer.start(Collections.singleton(GENERAL_KAFKA_STRING_TOPIC), consumerRecord -> consumedRecords.add(consumerRecord.value()));
    }

    @Test
    public void producedMessageHasBeenConsumed() throws Exception {
        restMockMvc.perform(post("/api/fixed-asset-service-kafka/publish-test?message=" + message)).andExpect(status().isOk());

        Map<MetricName, ? extends Metric> metrics = kafkaStringConsumer.getKafkaConsumer().metrics();

        Metric recordsConsumedTotalMetric = metrics.entrySet().stream().filter(entry -> "records-consumed-total".equals(entry.getKey().name())).findFirst().get().getValue();

        Double expectedTotalConsumedMessage = 1.0;
        Double totalConsumedMessage;
        int attempt = 0;
        do {
            totalConsumedMessage = (Double) recordsConsumedTotalMetric.metricValue();
            Thread.sleep(200);
        } while (!totalConsumedMessage.equals(expectedTotalConsumedMessage) && attempt++ < MAX_ATTEMPT);

        Assertions.assertThat(attempt).isLessThan(MAX_ATTEMPT);
        Assertions.assertThat(totalConsumedMessage).isEqualTo(expectedTotalConsumedMessage);
        Assertions.assertThat(consumedRecords.get(0)).isEqualTo(message);
    }


}
