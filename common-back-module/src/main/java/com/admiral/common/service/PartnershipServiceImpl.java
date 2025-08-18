package com.admiral.common.service;

import com.admiral.common.database.repository.PartnershipRepository;
import com.admiral.common.dto.partnership.CreatePartnershipDTO;
import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import com.admiral.common.mapper.PartnershipRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartnershipServiceImpl implements PartnershipService {

    private final PartnershipRepository partnershipRepository;
    private final PartnershipRequestMapper partnershipRequestMapper;

    @Transactional
    public ReadPartnershipDTO createPartnershipRequest(CreatePartnershipDTO createPartnershipDTO) {
        return partnershipRequestMapper.toDto(
                partnershipRepository.save(
                        partnershipRequestMapper.toEntity(createPartnershipDTO)));
    }

    @Override
    public List<ReadPartnershipDTO> getPartnershipRequests() {
        return partnershipRepository.findAll()
                .stream()
                .map(partnershipRequestMapper::toDto)
                .toList();
    }
}
