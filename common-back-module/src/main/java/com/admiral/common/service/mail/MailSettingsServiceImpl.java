package com.admiral.common.service.mail;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.database.repository.MailSettingsRepository;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import com.admiral.common.mapper.MailSettingsMapper;
import com.admiral.common.service.encrypt.MailPassowrdEncryptor;
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
    private final MailPassowrdEncryptor encryptor;

    public Optional<ReadMailSettingsDTO> getSettings() {
        return getSettingsForInnerUse().map(mailSettingsMapper::toDTO);
    }

    @Override
    public Optional<MailSettings> getSettingsForInnerUse() {
        return repository.findById(ID_SETTINGS);
    }

    @Transactional
    @Override
    public ReadMailSettingsDTO setSettings(CreateMailSettingsDTO newSettings) {
        return mailSettingsMapper.toDTO(updateSettings(newSettings));
    }

    @Transactional
    @Override
    public MailSettings setSettingsForInnerUse(CreateMailSettingsDTO newSettings) {
        return repository.save(updateSettings(newSettings));
    }

    private MailSettings updateSettings(CreateMailSettingsDTO newSettings) {
        return repository.findById(ID_SETTINGS)
                .map(foundSettings -> {
                    MailSettings settings = mailSettingsMapper.copy(newSettings, foundSettings);
                    // Если пароль пуст при обновлении существующих настроек, то оставляем старый
                    if (newSettings.getPassword() != null && !newSettings.getPassword().isEmpty()) {
                        settings.setPassword(encryptor.encrypt(newSettings.getPassword()));
                    }
                    return settings;
                })
                .orElseGet(() -> {
                    MailSettings settings = mailSettingsMapper.toEntity(newSettings);
                    settings.setId(ID_SETTINGS);
                    if (newSettings.getPassword() != null && !newSettings.getPassword().isEmpty()) {
                        settings.setPassword(encryptor.encrypt(newSettings.getPassword()));
                    } else {
                        // Если пароль пуст при первом создании настроек то это ошибка(пароль должен быть)
                        throw new RuntimeException("Password cannot be empty");
                    }
                    return settings;
                });
    }
}
