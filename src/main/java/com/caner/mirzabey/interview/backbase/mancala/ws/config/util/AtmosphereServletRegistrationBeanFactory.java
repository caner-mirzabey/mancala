package com.caner.mirzabey.interview.backbase.mancala.ws.config.util;

import org.atmosphere.cpr.AtmosphereServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.core.Ordered;

/**
 * Created by ecanmir on 11.06.2016.
 */
public class AtmosphereServletRegistrationBeanFactory {
    public static ServletRegistrationBean createServletRegistrationBean(String name, String urlMapping) {
        ServletRegistrationBean registration = new ServletRegistrationBean(new AtmosphereServlet(), urlMapping);
        registration.setName(name);
        registration.addInitParameter("org.atmosphere.cpr.objectFactory",
                                      "org.atmosphere.spring.SpringWebObjectFactory");
        registration.addInitParameter("org.atmosphere.cpr.packages", "com.caner.mirzabey.interview.backbase.mancala");
        registration.addInitParameter(
                "org.atmosphere.interceptor.HeartbeatInterceptor" + ".clientHeartbeatFrequencyInSeconds", "10");
        registration.setLoadOnStartup(0);
        // Need to occur before the EmbeddedAtmosphereInitializer
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
