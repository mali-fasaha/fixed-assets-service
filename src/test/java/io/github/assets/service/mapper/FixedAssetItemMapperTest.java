package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FixedAssetItemMapperTest {

    private FixedAssetItemMapper fixedAssetItemMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetItemMapper = new FixedAssetItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(fixedAssetItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetItemMapper.fromId(null)).isNull();
    }
}
