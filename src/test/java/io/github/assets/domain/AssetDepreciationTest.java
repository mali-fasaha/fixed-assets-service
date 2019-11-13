package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetDepreciationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDepreciation.class);
        AssetDepreciation assetDepreciation1 = new AssetDepreciation();
        assetDepreciation1.setId(1L);
        AssetDepreciation assetDepreciation2 = new AssetDepreciation();
        assetDepreciation2.setId(assetDepreciation1.getId());
        assertThat(assetDepreciation1).isEqualTo(assetDepreciation2);
        assetDepreciation2.setId(2L);
        assertThat(assetDepreciation1).isNotEqualTo(assetDepreciation2);
        assetDepreciation1.setId(null);
        assertThat(assetDepreciation1).isNotEqualTo(assetDepreciation2);
    }
}
