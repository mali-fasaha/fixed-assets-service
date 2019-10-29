package io.github.assets.app.messaging.core;

import java.io.Serializable;

public class TestMessage implements Serializable {
    private static final long serialVersionUID = 2159270365263883130L;
    private final String message;

    public TestMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestMessage{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestMessage)) {
            return false;
        }

        final TestMessage that = (TestMessage) o;

        return getMessage() != null ? getMessage().equals(that.getMessage()) : that.getMessage() == null;
    }

    @Override
    public int hashCode() {
        return getMessage() != null ? getMessage().hashCode() : 0;
    }
}
