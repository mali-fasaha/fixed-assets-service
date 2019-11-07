package io.github.assets.app.messaging.platform;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.messaging.fileNotification.FileNotification;
import io.github.assets.app.messaging.fileNotification.FileNotificationMessageService;
import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.messaging.sample.Greetings;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.MessageToken;
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
public class FileNotificationControllerIT {


    @Autowired
    private MessageService<TokenizableMessage<String>> fileNotificationMessageService;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private FileNotificationStreams fileNotificationStreams;
    @Autowired
    private TokenGenerator tokenGenerator;


    @Test
    public void callGreetingsService() throws Exception {

        long timestamp = System.currentTimeMillis();
        String fileId = "1001";
        String filename = "AssetAdditions2019.xlsx";
        String description = "Assets Acquired in FY 2019";

        final FileNotification fileNotification = FileNotification.builder()
                                                                  .description(description)
                                                                  .fileId(fileId)
                                                                  .filename(filename)
                                                                  .timestamp(timestamp)
                                                                  .build();

        final FileNotification unMutatedFileNotification = SerializationUtils.clone(fileNotification);

        MessageTokenDTO messageToken = fileNotificationMessageService.sendMessage(fileNotification);

        log.info("Message sent with the token: {}", messageToken.getTokenValue());

        Object payload = messageCollector.forChannel(fileNotificationStreams.outbound()).poll().getPayload();

        // Check that message-token has been created in the db
        assertThat(messageToken.getId()).isNotNull();
        assertThat(messageToken.getTokenValue()).isEqualTo(tokenGenerator.md5Digest(unMutatedFileNotification));
        assertThat(messageToken.getTimeSent()).isEqualTo(fileNotification.getTimestamp());
        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(description);
        assertThat(payload.toString()).containsSequence(messageToken.getTokenValue());
    }
}
