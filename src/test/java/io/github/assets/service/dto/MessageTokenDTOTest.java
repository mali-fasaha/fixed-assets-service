package io.github.assets.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.assets.web.rest.TestUtil;

public class MessageTokenDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageTokenDTO.class);
        MessageTokenDTO messageTokenDTO1 = new MessageTokenDTO();
        messageTokenDTO1.setId(1L);
        MessageTokenDTO messageTokenDTO2 = new MessageTokenDTO();
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(messageTokenDTO1.getId());
        assertThat(messageTokenDTO1).isEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(2L);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO1.setId(null);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
    }
}
