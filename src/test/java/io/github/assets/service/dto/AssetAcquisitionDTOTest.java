package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetAcquisitionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetAcquisitionDTO.class);
        AssetAcquisitionDTO assetAcquisitionDTO1 = new AssetAcquisitionDTO();
        assetAcquisitionDTO1.setId(1L);
        AssetAcquisitionDTO assetAcquisitionDTO2 = new AssetAcquisitionDTO();
        assertThat(assetAcquisitionDTO1).isNotEqualTo(assetAcquisitionDTO2);
        assetAcquisitionDTO2.setId(assetAcquisitionDTO1.getId());
        assertThat(assetAcquisitionDTO1).isEqualTo(assetAcquisitionDTO2);
        assetAcquisitionDTO2.setId(2L);
        assertThat(assetAcquisitionDTO1).isNotEqualTo(assetAcquisitionDTO2);
        assetAcquisitionDTO1.setId(null);
        assertThat(assetAcquisitionDTO1).isNotEqualTo(assetAcquisitionDTO2);
    }
}
