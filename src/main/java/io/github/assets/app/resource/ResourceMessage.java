package io.github.assets.app.resource;

import io.github.assets.domain.enumeration.FileModelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Resource wrapper for queued-resource-DTOs
 */
@Getter
@AllArgsConstructor
public class ResourceMessage<DTO> {

    // TODO create mapping
    private final DTO resourceMessage;
    private final FileModelType fileModelType;
}
