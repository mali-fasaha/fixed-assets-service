package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.stereotype.Component;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;

@Component("fileUploadMTOMapping")
public class FileUploadMTOMapping implements Mapping<FileUploadDTO, FileUploadMTO> {

    @Override
    public FileUploadDTO toValue1(final FileUploadMTO vs) {
        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setDescription(vs.getDescription());
        fileUploadDTO.setFileName(vs.getFileName());
        fileUploadDTO.setPeriodFrom(LocalDate.parse(vs.getPeriodFrom(), DATETIME_FORMATTER));
        fileUploadDTO.setPeriodTo(LocalDate.parse(vs.getPeriodTo(), DATETIME_FORMATTER));
        fileUploadDTO.setFileTypeId(vs.getFileTypeId());
        fileUploadDTO.setDataFile(vs.getDataFile());
        fileUploadDTO.setDataFileContentType(vs.getDataFileContentType());
        fileUploadDTO.setUploadProcessed(vs.getUploadSuccessful());
        fileUploadDTO.setUploadProcessed(vs.getUploadProcessed());
        fileUploadDTO.setUploadToken(vs.getMessageToken());
        return fileUploadDTO;
    }

    @Override
    public FileUploadMTO toValue2(final FileUploadDTO vs) {
        return FileUploadMTO.builder()
                            .description(vs.getDescription())
                            .messageToken(vs.getUploadToken())
                            .fileName(vs.getFileName())
                            .periodFrom(DATETIME_FORMATTER.format(vs.getPeriodFrom()))
                            .periodTo(DATETIME_FORMATTER.format(vs.getPeriodTo()))
                            .fileTypeId(vs.getFileTypeId())
                            .dataFile(vs.getDataFile())
                            .dataFileContentType(vs.getDataFileContentType())
                            .uploadSuccessful(vs.isUploadSuccessful())
                            .uploadProcessed(vs.isUploadProcessed())
                            .build();
    }
}
