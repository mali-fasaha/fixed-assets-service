package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class FixedAssetAssessmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixedAssetAssessment.class);
        FixedAssetAssessment fixedAssetAssessment1 = new FixedAssetAssessment();
        fixedAssetAssessment1.setId(1L);
        FixedAssetAssessment fixedAssetAssessment2 = new FixedAssetAssessment();
        fixedAssetAssessment2.setId(fixedAssetAssessment1.getId());
        assertThat(fixedAssetAssessment1).isEqualTo(fixedAssetAssessment2);
        fixedAssetAssessment2.setId(2L);
        assertThat(fixedAssetAssessment1).isNotEqualTo(fixedAssetAssessment2);
        fixedAssetAssessment1.setId(null);
        assertThat(fixedAssetAssessment1).isNotEqualTo(fixedAssetAssessment2);
    }
}
