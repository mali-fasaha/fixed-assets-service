package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.MuteListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service
public class FileNotificationListener implements MuteListener<FileNotification> {

    @StreamListener(FileNotificationStreams.INPUT)
    public void handleMessage(@Payload FileNotification fileNotification) {
        log.info("File notification received...");
    }
}
