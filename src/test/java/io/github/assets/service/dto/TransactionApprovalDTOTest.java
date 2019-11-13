package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class TransactionApprovalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionApprovalDTO.class);
        TransactionApprovalDTO transactionApprovalDTO1 = new TransactionApprovalDTO();
        transactionApprovalDTO1.setId(1L);
        TransactionApprovalDTO transactionApprovalDTO2 = new TransactionApprovalDTO();
        assertThat(transactionApprovalDTO1).isNotEqualTo(transactionApprovalDTO2);
        transactionApprovalDTO2.setId(transactionApprovalDTO1.getId());
        assertThat(transactionApprovalDTO1).isEqualTo(transactionApprovalDTO2);
        transactionApprovalDTO2.setId(2L);
        assertThat(transactionApprovalDTO1).isNotEqualTo(transactionApprovalDTO2);
        transactionApprovalDTO1.setId(null);
        assertThat(transactionApprovalDTO1).isNotEqualTo(transactionApprovalDTO2);
    }
}
