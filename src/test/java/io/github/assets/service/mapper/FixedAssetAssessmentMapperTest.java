package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FixedAssetAssessmentMapperTest {

    private FixedAssetAssessmentMapper fixedAssetAssessmentMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetAssessmentMapper = new FixedAssetAssessmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(fixedAssetAssessmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetAssessmentMapper.fromId(null)).isNull();
    }
}
