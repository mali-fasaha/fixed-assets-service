package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class ScannedDocumentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScannedDocument.class);
        ScannedDocument scannedDocument1 = new ScannedDocument();
        scannedDocument1.setId(1L);
        ScannedDocument scannedDocument2 = new ScannedDocument();
        scannedDocument2.setId(scannedDocument1.getId());
        assertThat(scannedDocument1).isEqualTo(scannedDocument2);
        scannedDocument2.setId(2L);
        assertThat(scannedDocument1).isNotEqualTo(scannedDocument2);
        scannedDocument1.setId(null);
        assertThat(scannedDocument1).isNotEqualTo(scannedDocument2);
    }
}
