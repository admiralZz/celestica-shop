package com.admiral.common.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partnership_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

    @Column
    private String location;

    @Column(nullable = false)
    private String cooperationType;

    @Column
    private String productCategory;

    @Column
    private String message;
} 