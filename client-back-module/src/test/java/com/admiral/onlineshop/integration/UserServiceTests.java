package com.admiral.onlineshop.integration;

import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@RequiredArgsConstructor
public class UserServiceTests extends IntegrationTest {
    private final UserService userService;

    @Test
    public void testGetUser() {
        ReadUserDTO userById = userService.getUserById(1L);

        Assertions.assertNotNull(userById);
    }
}
