package io.github.assets.app.messaging;

import io.github.assets.app.file.FileNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service
public class FileNotificationListener {

    // TODO Configure listener streams
    // TODO Configure payload parameter
//    @StreamListener(FileNotificationStreams.NOTIFICATION_INPUT)// TODO configure input
    public void handleFileNotification(FileNotification fileNotification) {
        // TODO Handle files
        log.info("File notification received...");
    }
}
