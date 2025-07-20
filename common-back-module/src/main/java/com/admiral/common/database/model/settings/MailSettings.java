package com.admiral.common.database.model.settings;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "mail_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailSettings {

    @Id
    private Long id; // устанавливаем вручную, чтобы гарантировать что будет только одна запись(id = 1L)

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol;
    private boolean auth;
    private boolean sslEnable;
}


