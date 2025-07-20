package com.admiral.adminshop.controller;

import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import com.admiral.common.service.mail.MailSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final MailSettingsService mailSettingsService;

    @GetMapping("/mail")
    public ResponseEntity<ReadMailSettingsDTO> getMailSettings() {
        ReadMailSettingsDTO settings = mailSettingsService.getSettings()
                .orElseThrow(() -> new IllegalStateException("Mail settings not found"));
        return ResponseEntity.ok(settings);
    }

    @PutMapping("/mail")
    public ResponseEntity<ReadMailSettingsDTO> updateMailSettings(@Validated @RequestBody
                                                                      CreateMailSettingsDTO newMailSettings) {
        return ResponseEntity.ok(mailSettingsService.setSettings(newMailSettings));
    }
}
