package com.admiral.common.event;

import com.admiral.common.dto.partnership.ReadPartnershipDTO;

public record PartnershipRequestCreatedEvent(ReadPartnershipDTO partnershipDTO) {
}
