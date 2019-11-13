package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FixedAssetCategoryMapperTest {

    private FixedAssetCategoryMapper fixedAssetCategoryMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetCategoryMapper = new FixedAssetCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(fixedAssetCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetCategoryMapper.fromId(null)).isNull();
    }
}
