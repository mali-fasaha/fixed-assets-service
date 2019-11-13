package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DepreciationRegimeMapperTest {

    private DepreciationRegimeMapper depreciationRegimeMapper;

    @BeforeEach
    public void setUp() {
        depreciationRegimeMapper = new DepreciationRegimeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(depreciationRegimeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(depreciationRegimeMapper.fromId(null)).isNull();
    }
}
