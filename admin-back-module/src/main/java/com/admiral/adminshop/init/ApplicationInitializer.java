package com.admiral.adminshop.init;

import com.admiral.adminshop.config.init.DefaultUserProperties;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationRunner {
    private final DefaultUserProperties defaultUserProperties;
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        addDefaultAdmin();
    }

    private void addDefaultAdmin() {
        if (!userService.existsByEmail(defaultUserProperties.email())) {
            ReadUserDTO readUserDTO = userService.registerAdmin(CreateUserDTO.builder()
                    .email(defaultUserProperties.email())
                    .password(defaultUserProperties.password())
                    .build());
            if (readUserDTO == null) {
                throw new IllegalStateException("Failed to register default user");
            }
            log.info("Default admin has been added");
        }
    }
}
