package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class ScannedDocumentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannedDocumentDTO.class);
        ScannedDocumentDTO scannedDocumentDTO1 = new ScannedDocumentDTO();
        scannedDocumentDTO1.setId(1L);
        ScannedDocumentDTO scannedDocumentDTO2 = new ScannedDocumentDTO();
        assertThat(scannedDocumentDTO1).isNotEqualTo(scannedDocumentDTO2);
        scannedDocumentDTO2.setId(scannedDocumentDTO1.getId());
        assertThat(scannedDocumentDTO1).isEqualTo(scannedDocumentDTO2);
        scannedDocumentDTO2.setId(2L);
        assertThat(scannedDocumentDTO1).isNotEqualTo(scannedDocumentDTO2);
        scannedDocumentDTO1.setId(null);
        assertThat(scannedDocumentDTO1).isNotEqualTo(scannedDocumentDTO2);
    }
}
