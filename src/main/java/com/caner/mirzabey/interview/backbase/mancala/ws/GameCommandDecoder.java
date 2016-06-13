package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;

import java.io.IOException;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class GameCommandDecoder implements Decoder<String, GameCommand> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public GameCommand decode(String s) {
        try {
            return mapper.readValue(s, GameCommand.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Deserializing error", e);
        }
    }
}
