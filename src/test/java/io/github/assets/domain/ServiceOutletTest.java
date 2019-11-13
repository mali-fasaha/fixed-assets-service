package io.github.assets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class ServiceOutletTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOutlet.class);
        ServiceOutlet serviceOutlet1 = new ServiceOutlet();
        serviceOutlet1.setId(1L);
        ServiceOutlet serviceOutlet2 = new ServiceOutlet();
        serviceOutlet2.setId(serviceOutlet1.getId());
        assertThat(serviceOutlet1).isEqualTo(serviceOutlet2);
        serviceOutlet2.setId(2L);
        assertThat(serviceOutlet1).isNotEqualTo(serviceOutlet2);
        serviceOutlet1.setId(null);
        assertThat(serviceOutlet1).isNotEqualTo(serviceOutlet2);
    }
}
