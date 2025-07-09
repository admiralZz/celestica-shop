package com.admiral.adminshop.config.dev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Класс который хранит applicationContext глобально, для удобства отладки
 */

@Slf4j
@ConditionalOnProperty(name = "app.debug.enable-global-context", havingValue = "true")
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.warn("ApplicationContextProvider activated");
        ApplicationContextProvider.applicationContext = applicationContext;
    }
}
