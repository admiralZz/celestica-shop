package com.admiral.common.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activation_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    @CreationTimestamp
    private Instant createdAt;

    private LocalDateTime expiryDate;

    @Column(name = "is_used", nullable = false)
    private boolean used = false;
}
