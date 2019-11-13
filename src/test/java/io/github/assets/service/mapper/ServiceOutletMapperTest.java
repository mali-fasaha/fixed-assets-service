package io.github.assets.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ServiceOutletMapperTest {

    private ServiceOutletMapper serviceOutletMapper;

    @BeforeEach
    public void setUp() {
        serviceOutletMapper = new ServiceOutletMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(serviceOutletMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(serviceOutletMapper.fromId(null)).isNull();
    }
}
