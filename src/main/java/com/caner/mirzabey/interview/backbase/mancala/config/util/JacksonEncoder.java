package com.caner.mirzabey.interview.backbase.mancala.config.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class JacksonEncoder implements Encoder<Object, String> {
    private static final Logger logger = LoggerFactory.getLogger(JacksonEncoder.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Object m) {
        logger.debug("encode m::", m == null ? "null" : m.toString());
        try {
            return mapper.writeValueAsString(m);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
