package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CwipTransferMapperTest {

    private CwipTransferMapper cwipTransferMapper;

    @BeforeEach
    public void setUp() {
        cwipTransferMapper = new CwipTransferMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(cwipTransferMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cwipTransferMapper.fromId(null)).isNull();
    }
}
