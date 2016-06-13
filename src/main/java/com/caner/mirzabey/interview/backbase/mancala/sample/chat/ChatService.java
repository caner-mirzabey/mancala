/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caner.mirzabey.interview.backbase.mancala.sample.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;

@ManagedService (path = "/chat", broadcaster = DefaultBroadcaster.class)
public class ChatService {

    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Ready
    public void onReady(final AtmosphereResource resource) {
        this.logger.info("Connected " + resource.uuid());
        Broadcaster broadcaster = resource.getBroadcaster();
        logger.debug(broadcaster.toString());
        AtmosphereRequest request = resource.getRequest();
        logger.debug("request::%s" + request.toString());
        logger.debug("\n\n");
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attribute = attributeNames.nextElement();
            logger.debug("attribute::" + attribute);
            logger.debug("value::" + request.getAttribute(attribute));
        }
        logger.debug("\n\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String attribute = headerNames.nextElement();
            logger.debug("attribute::" + attribute);
            logger.debug("value::" + request.getHeader(attribute));
        }
        logger.debug("\n\n");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (headerNames.hasMoreElements()) {
            String attribute = headerNames.nextElement();
            logger.debug("attribute::" + attribute);
            logger.debug("value::" + request.getParameter(attribute));
        }

        logger.debug(request.body().asString());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        this.logger.info("Client {} disconnected [{}]",
                         event.getResource().uuid(),
                         (event.isCancelled() ? "cancelled" : "closed"));
    }

    @org.atmosphere.config.service.Message (encoders = JacksonEncoderDecoder.class,
                                            decoders = JacksonEncoderDecoder.class)
    public Message onMessage(Message message) throws IOException {
        this.logger.info("Author {} sent message {}", message.getAuthor(), message.getMessage());
        return message;
    }

    public static class JacksonEncoderDecoder implements Encoder<Message, String>, Decoder<String, Message> {

        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public String encode(Message m) {
            try {
                return this.mapper.writeValueAsString(m);
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public Message decode(String s) {
            try {
                return this.mapper.readValue(s, Message.class);
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

    }

}
