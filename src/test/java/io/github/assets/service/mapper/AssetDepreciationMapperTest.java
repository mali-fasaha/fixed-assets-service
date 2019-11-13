package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AssetDepreciationMapperTest {

    private AssetDepreciationMapper assetDepreciationMapper;

    @BeforeEach
    public void setUp() {
        assetDepreciationMapper = new AssetDepreciationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(assetDepreciationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assetDepreciationMapper.fromId(null)).isNull();
    }
}
