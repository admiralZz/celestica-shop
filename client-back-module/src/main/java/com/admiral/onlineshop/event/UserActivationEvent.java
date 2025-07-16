package com.admiral.onlineshop.event;

import com.admiral.common.database.model.UserActivationToken;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserActivationEvent extends ApplicationEvent {
    private final UserActivationToken token;

    public UserActivationEvent(UserActivationToken token) {
        super(token);
        this.token = token;
    }
}
