package io.github.assets.app.messaging;

import io.github.assets.app.messaging.assetAcquisition.AssetAcquisitionMTO;
import io.github.assets.app.messaging.assetAcquisition.AssetAcquisitionResourceStreams;
import io.github.assets.app.messaging.assetDepreciation.AssetDepreciationResourceStreams;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.repository.search.MessageTokenSearchRepository;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceConfigurations {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private AssetAcquisitionResourceStreams assetAcquisitionResourceStreams;
    @Autowired
    private AssetDepreciationResourceStreams assetDepreciationResourceStreams;
    @Autowired
    private Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;

    @Bean("assetAcquisitionCreateMessageService")
    public MessageService<TokenizableMessage<String>> assetAcquisitionCreateMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundCreateResource(), messageTokenMapper);
    }

    @Bean("assetAcquisitionUpdateMessageService")
    public MessageService<TokenizableMessage<String>> assetAcquisitionUpdateMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundUpdateResource(), messageTokenMapper);
    }

    @Bean("assetAcquisitionDeleteMessageService")
    public MessageService<TokenizableMessage<String>> assetAcquisitionDeleteMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundDeleteResource(), messageTokenMapper);
    }

    @Bean("assetDepreciationCreateMessageService")
    public MessageService<TokenizableMessage<String>> assetDepreciationCreateMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetDepreciationResourceStreams.outboundCreateResource(), messageTokenMapper);
    }

    @Bean("assetDepreciationUpdateMessageService")
    public MessageService<TokenizableMessage<String>> assetDepreciationUpdateMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetDepreciationResourceStreams.outboundUpdateResource(), messageTokenMapper);
    }

    @Bean("assetDepreciationDeleteMessageService")
    public MessageService<TokenizableMessage<String>> assetDepreciationDeleteMessageService(){

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, assetDepreciationResourceStreams.outboundDeleteResource(), messageTokenMapper);
    }


}
