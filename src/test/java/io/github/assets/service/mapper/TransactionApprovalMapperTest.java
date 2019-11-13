package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class TransactionApprovalMapperTest {

    private TransactionApprovalMapper transactionApprovalMapper;

    @BeforeEach
    public void setUp() {
        transactionApprovalMapper = new TransactionApprovalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(transactionApprovalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(transactionApprovalMapper.fromId(null)).isNull();
    }
}
