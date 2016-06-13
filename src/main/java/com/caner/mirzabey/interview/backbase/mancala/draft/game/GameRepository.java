package com.caner.mirzabey.interview.backbase.mancala.draft.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ecanmir on 12.06.2016.
 */
@Service
public class GameRepository {
    public static final Logger logger = LoggerFactory.getLogger(GameRepository.class);

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game find(String name) {
        return games.get(name);
    }

    public boolean insert(Game game) {
        if (!StringUtils.isEmpty(game.getName()) && games.containsKey(game.getName())) {
            return false;
        }
        games.put(game.getName(), game);
        return true;
    }

    public Collection<Game> findAll() {
        return games.values();
    }

    public Game remove(Game game) {
        return games.remove(game.getName());
    }

    public Game remove(String name) {
        return games.remove(name);
    }

}
