package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetDisposalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDisposalDTO.class);
        AssetDisposalDTO assetDisposalDTO1 = new AssetDisposalDTO();
        assetDisposalDTO1.setId(1L);
        AssetDisposalDTO assetDisposalDTO2 = new AssetDisposalDTO();
        assertThat(assetDisposalDTO1).isNotEqualTo(assetDisposalDTO2);
        assetDisposalDTO2.setId(assetDisposalDTO1.getId());
        assertThat(assetDisposalDTO1).isEqualTo(assetDisposalDTO2);
        assetDisposalDTO2.setId(2L);
        assertThat(assetDisposalDTO1).isNotEqualTo(assetDisposalDTO2);
        assetDisposalDTO1.setId(null);
        assertThat(assetDisposalDTO1).isNotEqualTo(assetDisposalDTO2);
    }
}
