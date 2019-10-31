package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.MuteListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service
public class FileNotificationListener implements MuteListener<FileNotification> {

    // TODO Configure listener streams
    // TODO Configure payload parameter
//    @StreamListener(FileNotificationStreams.NOTIFICATION_INPUT)// TODO configure input
    public void handleMessage(FileNotification fileNotification) {
        // TODO Handle files
        log.info("File notification received...");
    }
}
