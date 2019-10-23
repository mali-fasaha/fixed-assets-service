package io.github.assets.app.resource;

import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.stereotype.Component;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;

@Component
public class FileUploadToQueueDTOMapper {

    public FileUploadQueueDTO toQueueDTO(FileUploadDTO fileUploadDTO) {

        return FileUploadQueueDTO.builder()
                                 .dataFile(fileUploadDTO.getDataFile())
                                 .dataFileContentType(fileUploadDTO.getDataFileContentType())
                                 .description(fileUploadDTO.getDescription())
                                 .fileName(fileUploadDTO.getFileName())
                                 .fileTypeId(String.valueOf(fileUploadDTO.getFileTypeId()))
                                 .periodFrom(DATETIME_FORMATTER.format(fileUploadDTO.getPeriodFrom()))
                                 .periodTo(DATETIME_FORMATTER.format(fileUploadDTO.getPeriodTo()))
                                 .uploadToken(fileUploadDTO.getUploadToken())
                                 .build();
    }
}
