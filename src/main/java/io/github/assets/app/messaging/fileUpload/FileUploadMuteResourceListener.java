package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.MuteResourceListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("fileUploadMuteResourceListener")
public class FileUploadMuteResourceListener implements MuteResourceListener<FileUploadMTO> {

    @Override
    public void createEntity(final FileUploadMTO dto) throws URISyntaxException {

    }

    @Override
    public void updateEntity(final FileUploadMTO dto) throws URISyntaxException {

    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO) {

    }
}
