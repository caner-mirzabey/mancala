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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Decode a String into a {@link ChatProtocol}.
 */
public class ProtocolDecoder implements Decoder<String, ChatProtocol> {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public ChatProtocol decode(String s) {
        try {
            return mapper.readValue(s, ChatProtocol.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
