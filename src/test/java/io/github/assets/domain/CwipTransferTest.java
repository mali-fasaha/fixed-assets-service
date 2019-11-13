package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class CwipTransferTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CwipTransfer.class);
        CwipTransfer cwipTransfer1 = new CwipTransfer();
        cwipTransfer1.setId(1L);
        CwipTransfer cwipTransfer2 = new CwipTransfer();
        cwipTransfer2.setId(cwipTransfer1.getId());
        assertThat(cwipTransfer1).isEqualTo(cwipTransfer2);
        cwipTransfer2.setId(2L);
        assertThat(cwipTransfer1).isNotEqualTo(cwipTransfer2);
        cwipTransfer1.setId(null);
        assertThat(cwipTransfer1).isNotEqualTo(cwipTransfer2);
    }
}
