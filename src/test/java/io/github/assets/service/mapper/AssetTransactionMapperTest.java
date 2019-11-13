package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AssetTransactionMapperTest {

    private AssetTransactionMapper assetTransactionMapper;

    @BeforeEach
    public void setUp() {
        assetTransactionMapper = new AssetTransactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(assetTransactionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assetTransactionMapper.fromId(null)).isNull();
    }
}
