package com.admiral.onlineshop.controller;

import com.admiral.common.dto.partnership.CreatePartnershipDTO;
import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import com.admiral.common.service.PartnershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/partnership")
@RequiredArgsConstructor
public class PartnershipController {

    private final PartnershipService partnershipService;

    @PostMapping()
    public ResponseEntity<ReadPartnershipDTO> createPartnershipRequest(@Valid @RequestBody CreatePartnershipDTO createPartnershipDTO) {
        ReadPartnershipDTO result = partnershipService.createPartnershipRequest(createPartnershipDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ReadPartnershipDTO>> getPartnershipRequest() {
        return ResponseEntity.ok(partnershipService.getPartnershipRequests());
    }

}
