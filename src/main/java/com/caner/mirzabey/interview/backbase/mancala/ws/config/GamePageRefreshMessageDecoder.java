package com.caner.mirzabey.interview.backbase.mancala.ws.config;

import com.caner.mirzabey.interview.backbase.mancala.ws.GamePageRefreshMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;

import java.io.IOException;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class GamePageRefreshMessageDecoder implements Decoder<String, GamePageRefreshMessage> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public GamePageRefreshMessage decode(String s) {
        try {
            return mapper.readValue(s, GamePageRefreshMessage.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Deserializing error", e);
        }
    }
}
