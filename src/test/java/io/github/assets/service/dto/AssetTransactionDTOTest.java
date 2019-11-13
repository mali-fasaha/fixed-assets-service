package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class AssetTransactionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetTransactionDTO.class);
        AssetTransactionDTO assetTransactionDTO1 = new AssetTransactionDTO();
        assetTransactionDTO1.setId(1L);
        AssetTransactionDTO assetTransactionDTO2 = new AssetTransactionDTO();
        assertThat(assetTransactionDTO1).isNotEqualTo(assetTransactionDTO2);
        assetTransactionDTO2.setId(assetTransactionDTO1.getId());
        assertThat(assetTransactionDTO1).isEqualTo(assetTransactionDTO2);
        assetTransactionDTO2.setId(2L);
        assertThat(assetTransactionDTO1).isNotEqualTo(assetTransactionDTO2);
        assetTransactionDTO1.setId(null);
        assertThat(assetTransactionDTO1).isNotEqualTo(assetTransactionDTO2);
    }
}
