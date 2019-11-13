package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetItem.class);
        FixedAssetItem fixedAssetItem1 = new FixedAssetItem();
        fixedAssetItem1.setId(1L);
        FixedAssetItem fixedAssetItem2 = new FixedAssetItem();
        fixedAssetItem2.setId(fixedAssetItem1.getId());
        assertThat(fixedAssetItem1).isEqualTo(fixedAssetItem2);
        fixedAssetItem2.setId(2L);
        assertThat(fixedAssetItem1).isNotEqualTo(fixedAssetItem2);
        fixedAssetItem1.setId(null);
        assertThat(fixedAssetItem1).isNotEqualTo(fixedAssetItem2);
    }
}
