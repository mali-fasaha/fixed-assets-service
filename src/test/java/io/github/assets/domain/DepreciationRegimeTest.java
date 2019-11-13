package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class DepreciationRegimeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepreciationRegime.class);
        DepreciationRegime depreciationRegime1 = new DepreciationRegime();
        depreciationRegime1.setId(1L);
        DepreciationRegime depreciationRegime2 = new DepreciationRegime();
        depreciationRegime2.setId(depreciationRegime1.getId());
        assertThat(depreciationRegime1).isEqualTo(depreciationRegime2);
        depreciationRegime2.setId(2L);
        assertThat(depreciationRegime1).isNotEqualTo(depreciationRegime2);
        depreciationRegime1.setId(null);
        assertThat(depreciationRegime1).isNotEqualTo(depreciationRegime2);
    }
}
