package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AssetAcquisitionMapperTest {

    private AssetAcquisitionMapper assetAcquisitionMapper;

    @BeforeEach
    public void setUp() {
        assetAcquisitionMapper = new AssetAcquisitionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(assetAcquisitionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assetAcquisitionMapper.fromId(null)).isNull();
    }
}
