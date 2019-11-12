package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("fileUploadEntityResource")
public class FileUploadEntityResource implements EntityResource<FileUploadMTO, DeleteMessageDTO> {

    @Override
    public void createEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

    }

    @Override
    public void updateEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

    }
}
