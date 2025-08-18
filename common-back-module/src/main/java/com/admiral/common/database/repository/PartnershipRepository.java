package com.admiral.common.database.repository;

import com.admiral.common.database.model.Category;
import com.admiral.common.database.model.PartnershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnershipRepository extends JpaRepository<PartnershipRequest, Long> {
}