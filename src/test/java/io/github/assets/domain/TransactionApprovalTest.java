package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class TransactionApprovalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionApproval.class);
        TransactionApproval transactionApproval1 = new TransactionApproval();
        transactionApproval1.setId(1L);
        TransactionApproval transactionApproval2 = new TransactionApproval();
        transactionApproval2.setId(transactionApproval1.getId());
        assertThat(transactionApproval1).isEqualTo(transactionApproval2);
        transactionApproval2.setId(2L);
        assertThat(transactionApproval1).isNotEqualTo(transactionApproval2);
        transactionApproval1.setId(null);
        assertThat(transactionApproval1).isNotEqualTo(transactionApproval2);
    }
}
