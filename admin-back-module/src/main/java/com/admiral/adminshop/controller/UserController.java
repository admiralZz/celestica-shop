package com.admiral.adminshop.controller;

import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ReadUserDTO>> getAllUsers() {
        try {
            List<ReadUserDTO> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage()))
                    .build();
        }
    }
}
