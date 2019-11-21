package io.github.assets.app.messaging.platform;

import com.google.common.collect.ImmutableList;
import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.jsonStrings.JsonStringStreams;
import io.github.assets.app.messaging.jsonStrings.StringMessageDTO;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class JsonStringsControllerIT {

    @Autowired
    private JsonStringStreams jsonStringStreams;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageService<TokenizableMessage<String>, MessageTokenDTO> jsonStringMessageService;

    @Test
    public void callJsonStringService() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        String description = "About the Starks";
        String timestamp = String.valueOf(System.currentTimeMillis());

        final String jsonString = GsonUtils.toJsonString(ImmutableList.of(message, description, timestamp));

        TokenizableMessage<String> jsonMessage = StringMessageDTO.builder().jsonString(jsonString).description(description).timestamp(Long.parseLong(timestamp)).build();

        final TokenizableMessage<String> unMutatedGreeting = SerializationUtils.clone(jsonMessage);

        MessageTokenDTO messageToken = jsonStringMessageService.sendMessage(jsonMessage);

        log.info("Message sent with the token: {}", messageToken.getTokenValue());

        Object payload = messageCollector.forChannel(jsonStringStreams.acquisitionsCreateOutbound()).poll().getPayload();

        // Check that message-token has been created in the db
        assertThat(messageToken.getId()).isNotNull();
        assertThat(messageToken.getTokenValue()).isEqualTo(tokenGenerator.md5Digest(unMutatedGreeting));
        //        assertThat(messageToken.getTimeSent()).isEqualTo(GsonUtils.stringToList(jsonString, String[].class).get(2));
        //        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(message);
        assertThat(payload.toString()).containsSequence(description);
        assertThat(payload.toString()).containsSequence(messageToken.getTokenValue());
    }
}
