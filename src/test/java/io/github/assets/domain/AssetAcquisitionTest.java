package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetAcquisitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAcquisition.class);
        AssetAcquisition assetAcquisition1 = new AssetAcquisition();
        assetAcquisition1.setId(1L);
        AssetAcquisition assetAcquisition2 = new AssetAcquisition();
        assetAcquisition2.setId(assetAcquisition1.getId());
        assertThat(assetAcquisition1).isEqualTo(assetAcquisition2);
        assetAcquisition2.setId(2L);
        assertThat(assetAcquisition1).isNotEqualTo(assetAcquisition2);
        assetAcquisition1.setId(null);
        assertThat(assetAcquisition1).isNotEqualTo(assetAcquisition2);
    }
}
