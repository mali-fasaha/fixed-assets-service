package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.platform.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileUploadMTO implements TokenizableMessage<String> {

    private String description;
    private long timestamp;
    // ? upload token in the DTO
    private String messageToken;
    private String fileName;
    private String periodFrom;
    private String periodTo;
    private Long fileTypeId;
    private byte[] dataFile;
    private String dataFileContentType;
    private Boolean uploadSuccessful;
    private Boolean uploadProcessed;
}
