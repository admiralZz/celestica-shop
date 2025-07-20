package com.admiral.common.service.mail;

import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;

import java.util.Optional;

public interface MailSettingsService {
    Optional<ReadMailSettingsDTO> getSettings();
    ReadMailSettingsDTO setSettings(CreateMailSettingsDTO settings);
}
