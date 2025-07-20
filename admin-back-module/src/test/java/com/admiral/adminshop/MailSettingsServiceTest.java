package com.admiral.adminshop;

import com.admiral.common.database.repository.MailSettingsRepository;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import com.admiral.common.service.mail.MailSettingsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
public class MailSettingsServiceTest extends IntegrationTest {
    private final MailSettingsService mailSettingsService;
    private final MailSettingsRepository mailSettingsRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void testGetDefaultMailSettings() throws Exception {
        Assertions.assertTrue(mailSettingsService.getSettings().isPresent());
        Assertions.assertEquals(1, mailSettingsRepository.findAll().size());
    }

    @Test
    public void testUpdateMailSettings() throws Exception {
        ReadMailSettingsDTO readMailSettingsDTO = mailSettingsService.getSettings().orElseThrow();
        String newHost = "updated-host.com";
        CreateMailSettingsDTO createMailSettingsDTO = CreateMailSettingsDTO.builder()
                .host(newHost)
                .port(readMailSettingsDTO.getPort())
                .username(readMailSettingsDTO.getUsername())
                .password(readMailSettingsDTO.getPassword())
                .protocol(readMailSettingsDTO.getProtocol())
                .auth(readMailSettingsDTO.isAuth())
                .sslEnable(readMailSettingsDTO.isSslEnable())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/settings/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMailSettingsDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ReadMailSettingsDTO updatedMailSettingsDTO = objectMapper.readValue(response.getContentAsString(), ReadMailSettingsDTO.class);
                    assertThat(updatedMailSettingsDTO.getHost()).isEqualTo(newHost);
                });
        Assertions.assertEquals(1, mailSettingsRepository.findAll().size());
    }


}
