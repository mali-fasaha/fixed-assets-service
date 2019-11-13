package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class CapitalWorkInProgressDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapitalWorkInProgressDTO.class);
        CapitalWorkInProgressDTO capitalWorkInProgressDTO1 = new CapitalWorkInProgressDTO();
        capitalWorkInProgressDTO1.setId(1L);
        CapitalWorkInProgressDTO capitalWorkInProgressDTO2 = new CapitalWorkInProgressDTO();
        assertThat(capitalWorkInProgressDTO1).isNotEqualTo(capitalWorkInProgressDTO2);
        capitalWorkInProgressDTO2.setId(capitalWorkInProgressDTO1.getId());
        assertThat(capitalWorkInProgressDTO1).isEqualTo(capitalWorkInProgressDTO2);
        capitalWorkInProgressDTO2.setId(2L);
        assertThat(capitalWorkInProgressDTO1).isNotEqualTo(capitalWorkInProgressDTO2);
        capitalWorkInProgressDTO1.setId(null);
        assertThat(capitalWorkInProgressDTO1).isNotEqualTo(capitalWorkInProgressDTO2);
    }
}
