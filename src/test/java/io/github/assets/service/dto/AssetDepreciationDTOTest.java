package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetDepreciationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDepreciationDTO.class);
        AssetDepreciationDTO assetDepreciationDTO1 = new AssetDepreciationDTO();
        assetDepreciationDTO1.setId(1L);
        AssetDepreciationDTO assetDepreciationDTO2 = new AssetDepreciationDTO();
        assertThat(assetDepreciationDTO1).isNotEqualTo(assetDepreciationDTO2);
        assetDepreciationDTO2.setId(assetDepreciationDTO1.getId());
        assertThat(assetDepreciationDTO1).isEqualTo(assetDepreciationDTO2);
        assetDepreciationDTO2.setId(2L);
        assertThat(assetDepreciationDTO1).isNotEqualTo(assetDepreciationDTO2);
        assetDepreciationDTO1.setId(null);
        assertThat(assetDepreciationDTO1).isNotEqualTo(assetDepreciationDTO2);
    }
}
