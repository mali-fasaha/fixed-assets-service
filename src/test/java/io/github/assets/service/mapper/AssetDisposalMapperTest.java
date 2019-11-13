package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AssetDisposalMapperTest {

    private AssetDisposalMapper assetDisposalMapper;

    @BeforeEach
    public void setUp() {
        assetDisposalMapper = new AssetDisposalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(assetDisposalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assetDisposalMapper.fromId(null)).isNull();
    }
}
