package com.admiral.common.service;

import com.admiral.common.dto.partnership.CreatePartnershipDTO;
import com.admiral.common.dto.partnership.ReadPartnershipDTO;

import java.util.List;

public interface PartnershipService {
    ReadPartnershipDTO createPartnershipRequest(CreatePartnershipDTO createPartnershipDTO);
    List<ReadPartnershipDTO> getPartnershipRequests();
}
