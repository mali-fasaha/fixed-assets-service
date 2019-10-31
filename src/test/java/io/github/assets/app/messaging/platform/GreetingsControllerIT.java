package io.github.assets.app.messaging.platform;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.sample.Greetings;
import io.github.assets.app.messaging.sample.GreetingsListener;
import io.github.assets.app.messaging.sample.GreetingsStreams;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.MessageToken;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class GreetingsControllerIT {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Autowired
    private MessageService<Greetings> greetingsService;

    @Mock
    private GreetingsListener greetingsListener;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private GreetingsStreams greetingsStreams;
    @Autowired
    private TokenGenerator tokenGenerator;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void callGreetingsAPI() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        long timestamp = System.currentTimeMillis();

        Greetings greeting = Greetings.builder().message(message).timestamp(timestamp).build();

        greetingsStreams.outbound().send(MessageBuilder.withPayload(greeting).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        Object payload = messageCollector.forChannel(greetingsStreams.outbound()).poll().getPayload();

        String receivedMessage = "{\"timestamp\":" + greeting.getTimestamp() + ",\"message\":\"There must always be a Stark in Winterfell\"}";

        assertThat(payload.toString()).isEqualTo(receivedMessage);
    }

    @Test
    public void callGreetingsService() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        long timestamp = System.currentTimeMillis();

        Greetings greeting = Greetings.builder().message(message).timestamp(timestamp).build();

//        greetingsStreams.outbound().send(MessageBuilder.withPayload(greeting).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        MessageToken messageToken = greetingsService.sendMessage(greeting);

        log.info("Message sent with the token: {}", messageToken.getTokenValue());

        Object payload = messageCollector.forChannel(greetingsStreams.outbound()).poll().getPayload();

        String receivedMessage = "{\"timestamp\":" + greeting.getTimestamp() + ",\"message\":\"There must always be a Stark in Winterfell\"}";

        assertThat(payload.toString()).isEqualTo(receivedMessage);

        // Check that message-token has been created in the db
        assertThat(messageToken.getId()).isNotNull();
        assertThat(messageToken.getTokenValue()).isEqualTo(tokenGenerator.md5Digest(greeting));
        assertThat(messageToken.getTimeSent()).isEqualTo(greeting.getTimestamp());
    }

}
