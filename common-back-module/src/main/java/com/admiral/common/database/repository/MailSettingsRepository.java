package com.admiral.common.database.repository;

import com.admiral.common.database.model.settings.MailSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSettingsRepository extends JpaRepository<MailSettings, Long> {
}
