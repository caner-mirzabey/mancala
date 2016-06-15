package com.caner.mirzabey.interview.backbase.mancala.ws.config;

import org.atmosphere.config.service.AtmosphereInterceptorService;
import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResource;

/**
 * Created by ecanmir on 15.06.2016.
 */
@AtmosphereInterceptorService
public class WebSocketInterceptor implements AtmosphereInterceptor {
    @Override
    public Action inspect(AtmosphereResource r) {
        return null;
    }

    @Override
    public void postInspect(AtmosphereResource r) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void configure(AtmosphereConfig config) {

    }
}
