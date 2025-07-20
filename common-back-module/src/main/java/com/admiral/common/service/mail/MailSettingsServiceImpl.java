package com.admiral.common.service.mail;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.database.repository.MailSettingsRepository;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import com.admiral.common.mapper.MailSettingsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailSettingsServiceImpl implements MailSettingsService {
    private static final Long ID_SETTINGS = 1L;
    private final MailSettingsRepository repository;
    private final MailSettingsMapper mailSettingsMapper;

    public Optional<ReadMailSettingsDTO> getSettings() {
        return repository.findById(ID_SETTINGS).map(mailSettingsMapper::toDTO);
    }

    @Transactional
    @Override
    public ReadMailSettingsDTO setSettings(CreateMailSettingsDTO newSettings) {
        MailSettings settings = repository.findById(ID_SETTINGS)
                .map(foundSettings -> mailSettingsMapper.copy(newSettings, foundSettings))
                .orElseGet(() -> {
                    MailSettings mailSettings = mailSettingsMapper.toEntity(newSettings);
                    mailSettings.setId(ID_SETTINGS);
                    return mailSettings;
                });
        return mailSettingsMapper.toDTO(repository.save(settings));
    }
}
