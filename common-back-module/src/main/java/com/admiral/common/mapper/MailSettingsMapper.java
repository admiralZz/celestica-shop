package com.admiral.common.mapper;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.dto.mail.ReadMailSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MailSettingsMapper {
    
    ReadMailSettingsDTO toDTO(MailSettings mailSettings);
    MailSettings toEntity(CreateMailSettingsDTO mailSettingsDTO);
    MailSettings copy(CreateMailSettingsDTO source, @MappingTarget MailSettings target);
}