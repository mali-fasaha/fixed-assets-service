package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DealerMapperTest {

    private DealerMapper dealerMapper;

    @BeforeEach
    public void setUp() {
        dealerMapper = new DealerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(dealerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(dealerMapper.fromId(null)).isNull();
    }
}
