package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Slf4j
@Transactional
@Service("fileUploadMutationResource")
public class FileUploadMutationResource implements MutationResource<FileUploadDTO> {

    @Override
    public MessageTokenDTO createAssetAcquisition(final FileUploadDTO fileUploadDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public MessageTokenDTO updateAssetAcquisition(final FileUploadDTO fileUploadDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public MessageTokenDTO deleteEntity(final Long id) {
        return null;
    }
}
