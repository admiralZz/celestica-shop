package com.admiral.adminshop;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.database.repository.MailSettingsRepository;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import com.admiral.common.service.encrypt.MailPassowrdEncryptor;
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
    private final MailPassowrdEncryptor mailPassowrdEncryptor;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void testGetDefaultMailSettings() throws Exception {
        Assertions.assertTrue(mailSettingsService.getSettings().isPresent());
        Assertions.assertEquals(1, mailSettingsRepository.findAll().size());
    }

    @Test
    public void testUpdateMailSettings() throws Exception {
        MailSettings mailSettings = mailSettingsService.getSettingsForInnerUse().orElseThrow();
        String newHost = "updated-host.com";
        CreateMailSettingsDTO createMailSettingsDTO = CreateMailSettingsDTO.builder()
                .host(newHost)
                .port(mailSettings.getPort())
                .username(mailSettings.getUsername())
                .password(mailSettings.getPassword())
                .protocol(mailSettings.getProtocol())
                .auth(mailSettings.isAuth())
                .sslEnable(mailSettings.isSslEnable())
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

    @Test
    public void testUpdateMailSettingsWithoutPassword() throws Exception {
        MailSettings mailSettings = mailSettingsService.getSettingsForInnerUse().orElseThrow();
        String newHost = "updated-host.com";
        String oldPassword = mailSettings.getPassword();
        CreateMailSettingsDTO createMailSettingsDTO = CreateMailSettingsDTO.builder()
                .host(newHost)
                .port(mailSettings.getPort())
                .username(mailSettings.getUsername())
                .protocol(mailSettings.getProtocol())
                .auth(mailSettings.isAuth())
                .sslEnable(mailSettings.isSslEnable())
                .build();
        assertThat(createMailSettingsDTO.getPassword()).isNull();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/settings/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMailSettingsDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ReadMailSettingsDTO updatedMailSettingsDTO = objectMapper.readValue(response.getContentAsString(), ReadMailSettingsDTO.class);
                    assertThat(updatedMailSettingsDTO.getHost()).isEqualTo(newHost);
                    assertThat(mailSettingsService.getSettingsForInnerUse().get().getPassword()).isEqualTo(oldPassword);
                });
        Assertions.assertEquals(1, mailSettingsRepository.findAll().size());
    }

    @Test
    public void testMailPasswordEncryptor() {
        String encrypted = mailPassowrdEncryptor.encrypt("1234");
        String decrypted = mailPassowrdEncryptor.decrypt(encrypted);
        assertThat(decrypted).isEqualTo("1234");
    }


}
