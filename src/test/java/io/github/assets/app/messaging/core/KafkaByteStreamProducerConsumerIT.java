package io.github.assets.app.messaging.core;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.web.rest.TestUtil;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.testcontainers.containers.KafkaContainer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.github.assets.app.AppConstants.GENERAL_KAFKA_STRING_TOPIC;
import static io.github.assets.web.rest.TestUtil.createFormattingConversionService;
import static org.apache.commons.lang3.SerializationUtils.serialize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class KafkaByteStreamProducerConsumerIT {

    private List<String> consumedRecords;

    private MockMvc restMockMvc;

    private static boolean started = false;

    private static KafkaContainer kafkaContainer;

    @Autowired
    private AppProducer<byte[]> kafkaByteStreamProducer;

    @Autowired
    private ReadableConsumer<String, String> kafkaStringConsumer;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

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
        TestResourceKafkaByteStream kafkaResource = new TestResourceKafkaByteStream(kafkaByteStreamProducer);

        this.restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource)
                                          .setControllerAdvice(exceptionTranslator)
                                          .setConversionService(createFormattingConversionService())
                                          .setMessageConverters(jacksonMessageConverter)
                                          .setValidator(validator).build();

        kafkaByteStreamProducer.init();

        consumedRecords = new LinkedList<>();

        kafkaStringConsumer.start(Collections.singleton(GENERAL_KAFKA_STRING_TOPIC), consumerRecord -> consumedRecords.add(consumerRecord.value()));
    }

    @Test
    public void producedMessageHasBeenConsumed() throws Exception {

        // Messages
        final TestMessage message_1 = new TestMessage("custom byte[] message test 1");

        restMockMvc.perform(post("/api/fixed-asset-service-kafka/publish-test-bytes" )
                                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                                .content(TestUtil.convertObjectToJsonBytes(message_1)))
                   .andExpect(status().isOk());

        Map<MetricName, ? extends Metric> metrics = kafkaStringConsumer.getKafkaConsumer().metrics();

        Metric recordsConsumedTotalMetric = metrics.entrySet().stream().filter(entry -> "records-consumed-total".equals(entry.getKey().name())).findFirst().get().getValue();

        Double expectedTotalConsumedMessage = 5.0;
        Double totalConsumedMessage;
        int attempt = 0;
        do {
            totalConsumedMessage = (Double) recordsConsumedTotalMetric.metricValue();
            Thread.sleep(200);
        } while (!totalConsumedMessage.equals(expectedTotalConsumedMessage) && attempt++ < MAX_ATTEMPT);

        assertThat(attempt).isLessThan(MAX_ATTEMPT);
        assertThat(totalConsumedMessage).isEqualTo(expectedTotalConsumedMessage);
        assertThat(consumedRecords.get(0)).isEqualTo(message_1.getMessage());
    }
}
