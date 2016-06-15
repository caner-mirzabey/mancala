package com.caner.mirzabey.interview.backbase.mancala.ws.config;

import com.caner.mirzabey.interview.backbase.mancala.ws.config.util.AtmosphereServletRegistrationBeanFactory;

import org.atmosphere.cpr.ContainerInitializer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;

/**
 * Created by ecanmir on 11.06.2016.
 */
@Configuration
public class GameWebSocketConfiguration {
    @Bean
    public ServletRegistrationBean atmosphereGameServlet() {
        return AtmosphereServletRegistrationBeanFactory.createServletRegistrationBean("game", "/game/*");
    }

    @Bean
    public GameWebSocketConfiguration.EmbeddedAtmosphereInitializer atmosphereInitializer() {
        return new GameWebSocketConfiguration.EmbeddedAtmosphereInitializer();
    }

    private static class EmbeddedAtmosphereInitializer extends ContainerInitializer
            implements ServletContextInitializer {

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            onStartup(Collections.<Class<?>> emptySet(), servletContext);
        }

    }
}
