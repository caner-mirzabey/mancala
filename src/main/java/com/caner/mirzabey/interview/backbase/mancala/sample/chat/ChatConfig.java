package com.caner.mirzabey.interview.backbase.mancala.sample.chat;

import com.caner.mirzabey.interview.backbase.mancala.config.util.AtmosphereServletRegistrationBeanFactory;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ecanmir on 10.06.2016.
 */
@Configuration
public class ChatConfig {

    @Bean
    public ServletRegistrationBean atmosphereChatServlet() {
        return AtmosphereServletRegistrationBeanFactory.createServletRegistrationBean("chat", "/chat/*");
    }

}
