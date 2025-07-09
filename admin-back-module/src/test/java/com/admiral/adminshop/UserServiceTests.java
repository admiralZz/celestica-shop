package com.admiral.adminshop;

import com.admiral.adminshop.dto.LoginRequestDto;
import com.admiral.adminshop.dto.ReadUserDTO;
import com.admiral.adminshop.service.UserService;
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
public class UserServiceTests extends IntegrationTest {
    private final UserService userService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void testGetUser() {
        ReadUserDTO userByEmail = userService.getUserByEmail(getCurrentUser().getUsername());

        Assertions.assertNotNull(userByEmail);
    }

    @Test
    public void testLoginNotAdmin() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("testuser@example.com", "123456");
        var request = objectMapper.writeValueAsBytes(loginRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        ).andExpect(MockMvcResultMatchers.status().is(401))
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    Assertions.assertNotNull(response.getContentAsString());
                    assertThat(response.getContentAsString()).isEqualTo("User not found with email: testuser@example.com");
                });
    }

    @Test
    public void testLoginAdmin() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("testadmin@example.com", "123456");
        var request = objectMapper.writeValueAsBytes(loginRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
