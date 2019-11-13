package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CapitalWorkInProgressMapperTest {

    private CapitalWorkInProgressMapper capitalWorkInProgressMapper;

    @BeforeEach
    public void setUp() {
        capitalWorkInProgressMapper = new CapitalWorkInProgressMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(capitalWorkInProgressMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(capitalWorkInProgressMapper.fromId(null)).isNull();
    }
}
