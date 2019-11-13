package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class CapitalWorkInProgressTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapitalWorkInProgress.class);
        CapitalWorkInProgress capitalWorkInProgress1 = new CapitalWorkInProgress();
        capitalWorkInProgress1.setId(1L);
        CapitalWorkInProgress capitalWorkInProgress2 = new CapitalWorkInProgress();
        capitalWorkInProgress2.setId(capitalWorkInProgress1.getId());
        assertThat(capitalWorkInProgress1).isEqualTo(capitalWorkInProgress2);
        capitalWorkInProgress2.setId(2L);
        assertThat(capitalWorkInProgress1).isNotEqualTo(capitalWorkInProgress2);
        capitalWorkInProgress1.setId(null);
        assertThat(capitalWorkInProgress1).isNotEqualTo(capitalWorkInProgress2);
    }
}
