package com.admiral.adminshop;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Sql({
        "classpath:sql/init_data.sql"
})
@Transactional
@WithMockUser(username = "testadmin@example.com", password = "123456", authorities = {"ROLE_ADMIN"})
public abstract class IntegrationTest {
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.4");

    @BeforeAll
    static void runContainer() throws Exception {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

    public User getCurrentUser() {
        return (User) Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .orElse(null);
    }

}
