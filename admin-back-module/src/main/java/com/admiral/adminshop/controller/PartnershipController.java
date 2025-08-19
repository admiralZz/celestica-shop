package com.admiral.adminshop.controller;

import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import com.admiral.common.service.PartnershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/partnership")
@RequiredArgsConstructor
public class PartnershipController {

    private final PartnershipService partnershipService;

    @GetMapping
    public ResponseEntity<List<ReadPartnershipDTO>> getPartnershipRequest() {
        return ResponseEntity.ok(partnershipService.getPartnershipRequests());
    }

}
