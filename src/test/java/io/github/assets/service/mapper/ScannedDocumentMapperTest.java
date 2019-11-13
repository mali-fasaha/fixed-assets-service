package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ScannedDocumentMapperTest {

    private ScannedDocumentMapper scannedDocumentMapper;

    @BeforeEach
    public void setUp() {
        scannedDocumentMapper = new ScannedDocumentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(scannedDocumentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(scannedDocumentMapper.fromId(null)).isNull();
    }
}
