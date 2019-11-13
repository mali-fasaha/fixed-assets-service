package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class CwipTransferDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CwipTransferDTO.class);
        CwipTransferDTO cwipTransferDTO1 = new CwipTransferDTO();
        cwipTransferDTO1.setId(1L);
        CwipTransferDTO cwipTransferDTO2 = new CwipTransferDTO();
        assertThat(cwipTransferDTO1).isNotEqualTo(cwipTransferDTO2);
        cwipTransferDTO2.setId(cwipTransferDTO1.getId());
        assertThat(cwipTransferDTO1).isEqualTo(cwipTransferDTO2);
        cwipTransferDTO2.setId(2L);
        assertThat(cwipTransferDTO1).isNotEqualTo(cwipTransferDTO2);
        cwipTransferDTO1.setId(null);
        assertThat(cwipTransferDTO1).isNotEqualTo(cwipTransferDTO2);
    }
}
