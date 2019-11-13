package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class DepreciationRegimeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationRegimeDTO.class);
        DepreciationRegimeDTO depreciationRegimeDTO1 = new DepreciationRegimeDTO();
        depreciationRegimeDTO1.setId(1L);
        DepreciationRegimeDTO depreciationRegimeDTO2 = new DepreciationRegimeDTO();
        assertThat(depreciationRegimeDTO1).isNotEqualTo(depreciationRegimeDTO2);
        depreciationRegimeDTO2.setId(depreciationRegimeDTO1.getId());
        assertThat(depreciationRegimeDTO1).isEqualTo(depreciationRegimeDTO2);
        depreciationRegimeDTO2.setId(2L);
        assertThat(depreciationRegimeDTO1).isNotEqualTo(depreciationRegimeDTO2);
        depreciationRegimeDTO1.setId(null);
        assertThat(depreciationRegimeDTO1).isNotEqualTo(depreciationRegimeDTO2);
    }
}
