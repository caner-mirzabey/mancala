package com.caner.mirzabey.interview.backbase.mancala.ws.config;

import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.managed.Decoder;

import java.io.IOException;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class GameDecoder implements Decoder<String, Game> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Game decode(String s) {
        try {
            return mapper.readValue(s, Game.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Deserializing error", e);
        }
    }
}
