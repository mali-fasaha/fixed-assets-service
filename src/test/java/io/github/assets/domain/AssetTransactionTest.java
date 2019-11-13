package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetTransactionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetTransaction.class);
        AssetTransaction assetTransaction1 = new AssetTransaction();
        assetTransaction1.setId(1L);
        AssetTransaction assetTransaction2 = new AssetTransaction();
        assetTransaction2.setId(assetTransaction1.getId());
        assertThat(assetTransaction1).isEqualTo(assetTransaction2);
        assetTransaction2.setId(2L);
        assertThat(assetTransaction1).isNotEqualTo(assetTransaction2);
        assetTransaction1.setId(null);
        assertThat(assetTransaction1).isNotEqualTo(assetTransaction2);
    }
}
