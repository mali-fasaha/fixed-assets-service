package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetInvoiceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetInvoiceDTO.class);
        FixedAssetInvoiceDTO fixedAssetInvoiceDTO1 = new FixedAssetInvoiceDTO();
        fixedAssetInvoiceDTO1.setId(1L);
        FixedAssetInvoiceDTO fixedAssetInvoiceDTO2 = new FixedAssetInvoiceDTO();
        assertThat(fixedAssetInvoiceDTO1).isNotEqualTo(fixedAssetInvoiceDTO2);
        fixedAssetInvoiceDTO2.setId(fixedAssetInvoiceDTO1.getId());
        assertThat(fixedAssetInvoiceDTO1).isEqualTo(fixedAssetInvoiceDTO2);
        fixedAssetInvoiceDTO2.setId(2L);
        assertThat(fixedAssetInvoiceDTO1).isNotEqualTo(fixedAssetInvoiceDTO2);
        fixedAssetInvoiceDTO1.setId(null);
        assertThat(fixedAssetInvoiceDTO1).isNotEqualTo(fixedAssetInvoiceDTO2);
    }
}
