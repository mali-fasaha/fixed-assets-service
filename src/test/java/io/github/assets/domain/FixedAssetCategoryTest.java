package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetCategory.class);
        FixedAssetCategory fixedAssetCategory1 = new FixedAssetCategory();
        fixedAssetCategory1.setId(1L);
        FixedAssetCategory fixedAssetCategory2 = new FixedAssetCategory();
        fixedAssetCategory2.setId(fixedAssetCategory1.getId());
        assertThat(fixedAssetCategory1).isEqualTo(fixedAssetCategory2);
        fixedAssetCategory2.setId(2L);
        assertThat(fixedAssetCategory1).isNotEqualTo(fixedAssetCategory2);
        fixedAssetCategory1.setId(null);
        assertThat(fixedAssetCategory1).isNotEqualTo(fixedAssetCategory2);
    }
}
