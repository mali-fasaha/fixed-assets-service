package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetItemDTO.class);
        FixedAssetItemDTO fixedAssetItemDTO1 = new FixedAssetItemDTO();
        fixedAssetItemDTO1.setId(1L);
        FixedAssetItemDTO fixedAssetItemDTO2 = new FixedAssetItemDTO();
        assertThat(fixedAssetItemDTO1).isNotEqualTo(fixedAssetItemDTO2);
        fixedAssetItemDTO2.setId(fixedAssetItemDTO1.getId());
        assertThat(fixedAssetItemDTO1).isEqualTo(fixedAssetItemDTO2);
        fixedAssetItemDTO2.setId(2L);
        assertThat(fixedAssetItemDTO1).isNotEqualTo(fixedAssetItemDTO2);
        fixedAssetItemDTO1.setId(null);
        assertThat(fixedAssetItemDTO1).isNotEqualTo(fixedAssetItemDTO2);
    }
}
