package com.caner.mirzabey.kalah.web.socket.config;

import com.caner.mirzabey.kalah.web.socket.message.GameAction;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;

import java.io.IOException;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class GameActionDecoder implements Decoder<String, GameAction> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public GameAction decode(String s) {
        try {
            return mapper.readValue(s, GameAction.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Deserializing error", e);
        }
    }
}
