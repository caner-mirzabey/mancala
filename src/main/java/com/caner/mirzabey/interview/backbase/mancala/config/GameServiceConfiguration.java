package com.caner.mirzabey.interview.backbase.mancala.config;

import com.caner.mirzabey.interview.backbase.mancala.config.util.AtmosphereServletRegistrationBeanFactory;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ecanmir on 11.06.2016.
 */
@Configuration
public class GameServiceConfiguration {
    @Bean
    public ServletRegistrationBean atmosphereGameServlet() {
        return AtmosphereServletRegistrationBeanFactory.createServletRegistrationBean("game", "/game/*");
    }
}
