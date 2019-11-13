package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetDisposalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDisposal.class);
        AssetDisposal assetDisposal1 = new AssetDisposal();
        assetDisposal1.setId(1L);
        AssetDisposal assetDisposal2 = new AssetDisposal();
        assetDisposal2.setId(assetDisposal1.getId());
        assertThat(assetDisposal1).isEqualTo(assetDisposal2);
        assetDisposal2.setId(2L);
        assertThat(assetDisposal1).isNotEqualTo(assetDisposal2);
        assetDisposal1.setId(null);
        assertThat(assetDisposal1).isNotEqualTo(assetDisposal2);
    }
}
