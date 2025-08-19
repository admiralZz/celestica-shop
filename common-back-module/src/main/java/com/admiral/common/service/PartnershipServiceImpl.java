package com.admiral.common.service;

import com.admiral.common.database.repository.PartnershipRepository;
import com.admiral.common.dto.partnership.CreatePartnershipDTO;
import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import com.admiral.common.event.PartnershipRequestCreatedEvent;
import com.admiral.common.mapper.PartnershipRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartnershipServiceImpl implements PartnershipService {

    private final ApplicationEventPublisher eventPublisher;
    private final PartnershipRepository partnershipRepository;
    private final PartnershipRequestMapper partnershipRequestMapper;

    @Transactional
    public ReadPartnershipDTO createPartnershipRequest(CreatePartnershipDTO createPartnershipDTO) {
        ReadPartnershipDTO partnershipDTO = partnershipRequestMapper.toDto(
                partnershipRepository.save(
                        partnershipRequestMapper.toEntity(createPartnershipDTO)));
        // отправка нотификации
        eventPublisher.publishEvent(new PartnershipRequestCreatedEvent(partnershipDTO));

        return partnershipDTO;
    }

    @Override
    public List<ReadPartnershipDTO> getPartnershipRequests() {
        return partnershipRepository.findAll()
                .stream()
                .map(partnershipRequestMapper::toDto)
                .toList();
    }
}
