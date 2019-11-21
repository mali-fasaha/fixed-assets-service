package io.github.assets.app.messaging.platform;

import io.github.assets.service.MessageTokenQueryService;
import io.github.assets.service.dto.MessageTokenCriteria;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final MessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final MessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public MessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        MessageTokenCriteria tokenValueCriteria = new MessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}
