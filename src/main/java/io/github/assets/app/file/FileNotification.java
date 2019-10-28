package io.github.assets.app.file;

import io.github.assets.domain.MessageToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileNotification implements Tokenizable, Serializable {
    private static final long serialVersionUID = -6472961232578342431L;
    private String fileId;

    private String timeOfUpload;

    private String filename;

    private String messageToken;

    private String description;
}
