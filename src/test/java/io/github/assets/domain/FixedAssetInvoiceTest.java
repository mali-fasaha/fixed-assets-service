package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetInvoiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetInvoice.class);
        FixedAssetInvoice fixedAssetInvoice1 = new FixedAssetInvoice();
        fixedAssetInvoice1.setId(1L);
        FixedAssetInvoice fixedAssetInvoice2 = new FixedAssetInvoice();
        fixedAssetInvoice2.setId(fixedAssetInvoice1.getId());
        assertThat(fixedAssetInvoice1).isEqualTo(fixedAssetInvoice2);
        fixedAssetInvoice2.setId(2L);
        assertThat(fixedAssetInvoice1).isNotEqualTo(fixedAssetInvoice2);
        fixedAssetInvoice1.setId(null);
        assertThat(fixedAssetInvoice1).isNotEqualTo(fixedAssetInvoice2);
    }
}
