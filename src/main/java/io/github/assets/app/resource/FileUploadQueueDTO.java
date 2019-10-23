package io.github.assets.app.resource;

import io.github.assets.domain.enumeration.FileModelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement
public class FileUploadQueueDTO {

    private String description;

    private String fileName;

    private String periodFrom;

    private String periodTo;

    private String fileTypeId;

    private String uploadToken;

    private byte[] dataFile;

    private String dataFileContentType;
}
