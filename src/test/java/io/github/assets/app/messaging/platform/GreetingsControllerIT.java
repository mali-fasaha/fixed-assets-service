package io.github.assets.app.messaging.platform;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.messaging.sample.GreetingsController;
import io.github.assets.app.messaging.sample.GreetingsListener;
import io.github.assets.app.messaging.sample.GreetingsService;
import io.github.assets.app.messaging.sample.GreetingsStreams;
import io.github.assets.app.messaging.sample.Greetings;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.web.rest.TestUtil;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.Validator;

import static io.github.assets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class GreetingsControllerIT {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc greetingsControllerMockMVC;

    @Autowired
    private GreetingsService greetingsService;

    @Mock
    private GreetingsListener greetingsListener;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private GreetingsStreams greetingsStreams;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GreetingsController greetingsController = new GreetingsController(greetingsService);
        this.greetingsControllerMockMVC = MockMvcBuilders.standaloneSetup(greetingsController)
                                                         .setControllerAdvice(exceptionTranslator)
                                                         .setConversionService(createFormattingConversionService())
                                                         .setMessageConverters(jacksonMessageConverter)
                                                         .setValidator(validator)
                                                         .build();
    }

    @Test
    public void callGreetingsAPI() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        long timestamp = System.currentTimeMillis();

        Greetings greeting = Greetings.builder().message(message).timestamp(timestamp).build();

        greetingsStreams.outboundGreetings().send(MessageBuilder.withPayload(greeting).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        Object payload = messageCollector.forChannel(greetingsStreams.outboundGreetings()).poll().getPayload();

        String receivedMessage = "{\"timestamp\":"+greeting.getTimestamp()+",\"message\":\"There must always be a Stark in Winterfell\"}";

        assertThat(payload.toString()).isEqualTo(receivedMessage);
    }

    @Test
    public void callGreetingsAPIFromController() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        long timestamp = System.currentTimeMillis();

        Greetings greeting = Greetings.builder().message(message).timestamp(timestamp).build();

        greetingsControllerMockMVC.perform(get("/greetings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(greeting))).andExpect(status().is(400));

        Object payload = messageCollector.forChannel(greetingsStreams.outboundGreetings()).poll().getPayload();

        String receivedMessage = "{\"timestamp\":"+greeting.getTimestamp()+",\"message\":\"There must always be a Stark in Winterfell\"}";

        assertThat(payload.toString()).isEqualTo(receivedMessage);
    }
}
