package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetAssessmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetAssessmentDTO.class);
        FixedAssetAssessmentDTO fixedAssetAssessmentDTO1 = new FixedAssetAssessmentDTO();
        fixedAssetAssessmentDTO1.setId(1L);
        FixedAssetAssessmentDTO fixedAssetAssessmentDTO2 = new FixedAssetAssessmentDTO();
        assertThat(fixedAssetAssessmentDTO1).isNotEqualTo(fixedAssetAssessmentDTO2);
        fixedAssetAssessmentDTO2.setId(fixedAssetAssessmentDTO1.getId());
        assertThat(fixedAssetAssessmentDTO1).isEqualTo(fixedAssetAssessmentDTO2);
        fixedAssetAssessmentDTO2.setId(2L);
        assertThat(fixedAssetAssessmentDTO1).isNotEqualTo(fixedAssetAssessmentDTO2);
        fixedAssetAssessmentDTO1.setId(null);
        assertThat(fixedAssetAssessmentDTO1).isNotEqualTo(fixedAssetAssessmentDTO2);
    }
}
