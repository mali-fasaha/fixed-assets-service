package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetCategoryDTO.class);
        FixedAssetCategoryDTO fixedAssetCategoryDTO1 = new FixedAssetCategoryDTO();
        fixedAssetCategoryDTO1.setId(1L);
        FixedAssetCategoryDTO fixedAssetCategoryDTO2 = new FixedAssetCategoryDTO();
        assertThat(fixedAssetCategoryDTO1).isNotEqualTo(fixedAssetCategoryDTO2);
        fixedAssetCategoryDTO2.setId(fixedAssetCategoryDTO1.getId());
        assertThat(fixedAssetCategoryDTO1).isEqualTo(fixedAssetCategoryDTO2);
        fixedAssetCategoryDTO2.setId(2L);
        assertThat(fixedAssetCategoryDTO1).isNotEqualTo(fixedAssetCategoryDTO2);
        fixedAssetCategoryDTO1.setId(null);
        assertThat(fixedAssetCategoryDTO1).isNotEqualTo(fixedAssetCategoryDTO2);
    }
}
