package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FixedAssetInvoiceMapperTest {

    private FixedAssetInvoiceMapper fixedAssetInvoiceMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetInvoiceMapper = new FixedAssetInvoiceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(fixedAssetInvoiceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetInvoiceMapper.fromId(null)).isNull();
    }
}
