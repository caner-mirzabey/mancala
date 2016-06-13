package com.caner.mirzabey.interview.backbase.mancala.sample.multichat;

import com.caner.mirzabey.interview.backbase.mancala.config.util.AtmosphereServletRegistrationBeanFactory;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ecanmir on 10.06.2016.
 */
@Configuration
public class ChatRoomConfig {

    @Bean
    public ServletRegistrationBean atmosphereMultiChatServlet() {
        return AtmosphereServletRegistrationBeanFactory.createServletRegistrationBean("chat-room", "/chat-room/*");
    }

}
