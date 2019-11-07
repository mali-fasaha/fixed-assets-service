package io.github.assets.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.assets.domain.MessageToken} entity.
 */
public class MessageTokenDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private Long timeSent;

    @NotNull
    private String tokenValue;

    private Boolean received;

    private Boolean actioned;

    private Boolean contentFullyEnqueued;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Long timeSent) {
        this.timeSent = timeSent;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Boolean isReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean isActioned() {
        return actioned;
    }

    public void setActioned(Boolean actioned) {
        this.actioned = actioned;
    }

    public Boolean isContentFullyEnqueued() {
        return contentFullyEnqueued;
    }

    public void setContentFullyEnqueued(Boolean contentFullyEnqueued) {
        this.contentFullyEnqueued = contentFullyEnqueued;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageTokenDTO messageTokenDTO = (MessageTokenDTO) o;
        if (messageTokenDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageTokenDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageTokenDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", timeSent=" + getTimeSent() +
            ", tokenValue='" + getTokenValue() + "'" +
            ", received='" + isReceived() + "'" +
            ", actioned='" + isActioned() + "'" +
            ", contentFullyEnqueued='" + isContentFullyEnqueued() + "'" +
            "}";
    }
}
