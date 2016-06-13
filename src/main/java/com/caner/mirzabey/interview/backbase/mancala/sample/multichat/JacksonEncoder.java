/*
 * Copyright 2016 Async-IO.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.caner.mirzabey.interview.backbase.mancala.sample.multichat;

import com.caner.mirzabey.interview.backbase.mancala.sample.multichat.JacksonEncoder.Encodable;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Encode a {@link ChatProtocol} into a String
 */
public class JacksonEncoder implements Encoder<Encodable, String> {
    private static final Logger logger = LoggerFactory.getLogger(JacksonEncoder.class);

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String encode(Encodable m) {
        logger.debug("encode m::", m == null ? "null" : m.toString());
        try {
            return mapper.writeValueAsString(m);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marker interface for Jackson.
     */
    public static interface Encodable {
    }
}
