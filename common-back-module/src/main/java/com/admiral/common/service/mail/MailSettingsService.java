package com.admiral.common.service.mail;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;

import java.util.Optional;

public interface MailSettingsService {
    Optional<ReadMailSettingsDTO> getSettings();
    Optional<MailSettings> getSettingsForInnerUse();
    ReadMailSettingsDTO setSettings(CreateMailSettingsDTO settings);
    MailSettings setSettingsForInnerUse(CreateMailSettingsDTO settings);
}
