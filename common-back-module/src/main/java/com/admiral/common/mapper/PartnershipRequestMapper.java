package com.admiral.common.mapper;

import com.admiral.common.database.model.Category;
import com.admiral.common.database.model.PartnershipRequest;
import com.admiral.common.dto.CategoryDTO;
import com.admiral.common.dto.partnership.CreatePartnershipDTO;
import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartnershipRequestMapper {
    
    // DTO -> Entity
    @Mapping(target = "id", ignore = true) // ID назначается базой данных
    PartnershipRequest toEntity(CreatePartnershipDTO createPartnershipDTO);

    ReadPartnershipDTO toDto(PartnershipRequest partnershipRequest);
}