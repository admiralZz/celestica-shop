package com.admiral.common.mapper;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MailSettingsMapper {
    @Mapping(target = "password", ignore = true) // логика установки пароля делается вручную в сервисе
    MailSettings toEntity(CreateMailSettingsDTO mailSettingsDTO);
    @Mapping(target = "password", ignore = true) // логика установки пароля делается вручную в сервисе
    MailSettings copy(CreateMailSettingsDTO source, @MappingTarget MailSettings target);

    default ReadMailSettingsDTO toDTO(MailSettings mailSettings) {
        return ReadMailSettingsDTO.builder()
                .host(mailSettings.getHost())
                .port(mailSettings.getPort())
                .username(mailSettings.getUsername())
                .hasPassword(mailSettings.getPassword() != null && !mailSettings.getPassword().isBlank())
                .protocol(mailSettings.getProtocol())
                .auth(mailSettings.isAuth())
                .sslEnable(mailSettings.isSslEnable())
                .build();
    }
}