package com.caner.mirzabey.interview.backbase.mancala.config;

import org.atmosphere.cpr.ContainerInitializer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;

/**
 * Created by ecanmir on 10.06.2016.
 */
@Configuration
public class AtmosphereConfig {

    @Bean
    public AtmosphereConfig.EmbeddedAtmosphereInitializer atmosphereInitializer() {
        return new AtmosphereConfig.EmbeddedAtmosphereInitializer();
    }

    private static class EmbeddedAtmosphereInitializer extends ContainerInitializer
            implements ServletContextInitializer {

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            onStartup(Collections.<Class<?>> emptySet(), servletContext);
        }

    }

}
