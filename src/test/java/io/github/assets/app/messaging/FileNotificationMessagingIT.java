package io.github.assets.app.messaging;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.file.FileNotification;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
class FileNotificationMessagingIT {

    @Autowired
    private FileNotificationStreams fileNotificationStreams;

    @Autowired
    private MessageCollector messageCollector;

    @Test
    void testMessages() {

        BlockingQueue<Message<?>> messages = messageCollector.forChannel(fileNotificationStreams.outboundNotifications());

        assertThat(messages, receivesPayloadThat(is(new FileNotification())));
    }

    //    @BeforeEach
    //    void setUp() {
    //    }
    //
    //    @AfterEach
    //    void tearDown() {
    //    }
}
