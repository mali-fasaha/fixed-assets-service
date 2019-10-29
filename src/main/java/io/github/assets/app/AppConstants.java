package io.github.assets.app;

import java.time.format.DateTimeFormatter;

public class AppConstants {

    // Regex for acceptable logins
    public static final String EMAIL_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final int TOKEN_BYTE_LENGTH = 20;
    public static final String DATETIME_FORMAT = "yyyy/MM/dd";
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    // Kafka Topics
    public static final String GENERAL_KAFKA_STRING_TOPIC = "topic_fixedassetservice";

    private AppConstants() {
    }
}
