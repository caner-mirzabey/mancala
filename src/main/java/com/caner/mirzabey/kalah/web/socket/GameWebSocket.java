package com.caner.mirzabey.kalah.web.socket;

import com.caner.mirzabey.kalah.game.GameEngineService;
import com.caner.mirzabey.kalah.game.GameRepository;
import com.caner.mirzabey.kalah.game.data.Game;
import com.caner.mirzabey.kalah.game.exception.GameException;
import com.caner.mirzabey.kalah.user.UserRepository;
import com.caner.mirzabey.kalah.web.socket.config.GameActionDecoder;
import com.caner.mirzabey.kalah.web.socket.config.util.JacksonEncoder;
import com.caner.mirzabey.kalah.web.socket.message.GameAction;
import com.caner.mirzabey.kalah.web.socket.message.GameMessage;
import com.caner.mirzabey.kalah.web.socket.message.GameMessage.Event;

import org.atmosphere.config.service.*;
import org.atmosphere.config.service.DeliverTo.DELIVER_TO;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.caner.mirzabey.kalah.web.socket.GameWebSocket.PATH;

/**
 * Created by ecanmir on 11.06.2016.
 */
@ManagedService (path = PATH)
public class GameWebSocket {
    public static final Logger logger = LoggerFactory.getLogger(GameWebSocket.class);

    public static final String PATH                       = "/game/{name: [a-zA-Z][a-zA-Z_0-9]*}";
    public static final String CONTEXT_PATH               = "/game";
    public static final String USERNAME_REQUEST_PARAMETER = "username";
    public static final String LOGIN_PATH                 = "/game/LOGIN";
    public static final String LOGIN_GAME_NAME            = "LOGIN";

    @PathParam ("name")
    private String name;

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @DeliverTo (DELIVER_TO.BROADCASTER)
    @Ready (encoders = {JacksonEncoder.class})
    public GameMessage onReady(AtmosphereResource resource) {
        this.logger.info("Connected uuid::{}", resource.uuid());
        Map<String, Collection> usersAndGames = new HashMap<>();
        usersAndGames.put("users", userRepository.findAll());
        usersAndGames.put("games", gameRepository.findAll());
        return new GameMessage("Greetings...", usersAndGames, Event.LOGIN);
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        String username = event.getResource().getRequest().getParameter(USERNAME_REQUEST_PARAMETER);
        logger.debug("onDisconnect --> Connection dropped event::{}, uuid::{}, username::{}",
                     event,
                     event.getResource().uuid(),
                     username);
    }

    @Resume
    public void onResume(AtmosphereResource resource) {
        logger.debug("onResume --> AtmosphereResource::{}", resource.uuid());
        String username = resource.getRequest().getParameter("username");
        logger.debug("username::{}", username);
    }

    @DeliverTo (DELIVER_TO.BROADCASTER)
    @Message (encoders = JacksonEncoder.class, decoders = GameActionDecoder.class)
    public Game onGameAction(GameAction gameAction) throws GameException {
        Game game = gameEngineService.action(gameAction);
        if (game != null) {
            return game;
        }
        return null;
    }

}
