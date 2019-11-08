package io.github.assets.service.mapper;

import io.github.assets.domain.*;
import io.github.assets.service.dto.MessageTokenDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageToken} and its DTO {@link MessageTokenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MessageTokenMapper extends EntityMapper<MessageTokenDTO, MessageToken> {



    default MessageToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        MessageToken messageToken = new MessageToken();
        messageToken.setId(id);
        return messageToken;
    }
}
