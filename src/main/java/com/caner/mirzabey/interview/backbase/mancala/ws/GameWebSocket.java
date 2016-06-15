package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.caner.mirzabey.interview.backbase.mancala.game.GameEngineService;
import com.caner.mirzabey.interview.backbase.mancala.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.caner.mirzabey.interview.backbase.mancala.game.exception.GameException;
import com.caner.mirzabey.interview.backbase.mancala.user.User;
import com.caner.mirzabey.interview.backbase.mancala.user.UserRepository;
import com.caner.mirzabey.interview.backbase.mancala.ws.config.GameActionDecoder;
import com.caner.mirzabey.interview.backbase.mancala.ws.config.util.JacksonEncoder;

import org.atmosphere.config.service.*;
import org.atmosphere.config.service.DeliverTo.DELIVER_TO;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

import static com.caner.mirzabey.interview.backbase.mancala.ws.GameWebSocket.PATH;

/**
 * Created by ecanmir on 11.06.2016.
 */
@ManagedService (path = PATH, broadcaster = DefaultBroadcaster.class)
public class GameWebSocket {
    public static final Logger logger = LoggerFactory.getLogger(GameWebSocket.class);

    public static final String PATH                       = "/game/{name: [a-zA-Z][a-zA-Z_0-9]*}";
    public static final String CONTEXT_PATH               = "/game";
    public static final String USERNAME_REQUEST_PARAMETER = "username";
    public static final String LOGIN_PATH                 = "/game/all";

    @PathParam ("name")
    private String name;

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    public static boolean isValidBroadcaster(Broadcaster broadcaster) {
        if (broadcaster.getID().startsWith(CONTEXT_PATH) && !(PATH.equals(broadcaster.getID()) || LOGIN_PATH.equals(
                broadcaster.getID()))) {
            return true;
        }
        return false;
    }

    public static String getGameNameFromBroadcasterID(String broadcasterID) throws GameException {
        if (!StringUtils.isEmpty(broadcasterID)) {
            String[] splitted = broadcasterID.split("/");
            if (splitted.length == 3 && !StringUtils.isEmpty(splitted[2])) {
                return splitted[2];
            }
        }
        throw new GameException("Game name cannot be empty/null");
    }

    @DeliverTo (DELIVER_TO.ALL)
    @Ready (encoders = {JacksonEncoder.class})
    public GamePageRefreshMessage onReady(AtmosphereResource resource) {
        this.logger.info("Connected uuid::{}", resource.uuid());
        String username = resource.getRequest().getParameter(GameWebSocket.USERNAME_REQUEST_PARAMETER);
        if (resource.getBroadcaster().getID().startsWith(GameWebSocket.CONTEXT_PATH)) {
            userRepository.insert(new User(resource.uuid(), username));
        }
        return new GamePageRefreshMessage(resource.uuid(),
                                          username,
                                          "Greetings...",
                                          new ArrayList<>(userRepository.findAll()),
                                          new ArrayList<>(gameRepository.findAll()));
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        String username = event.getResource().getRequest().getParameter(USERNAME_REQUEST_PARAMETER);
        logger.debug("Connection dropped uuid::{}, username::{}", event.getResource().uuid(), username);
        userRepository.remove(event.getResource().uuid());
        GamePageRefreshMessage message = new GamePageRefreshMessage(event.getResource().uuid(),
                                                                    username,
                                                                    "User dropped out...",
                                                                    new ArrayList<>(userRepository.findAll()),
                                                                    new ArrayList<>(gameRepository.findAll()));
        event.broadcaster().broadcast(new JacksonEncoder().encode(message));
    }

    @Resume
    public void onResume(AtmosphereResource resource) {
        logger.debug("AtmosphereResource::{}", resource.uuid());
        String username = resource.getRequest().getParameter("username");
        logger.debug("username::{}", username);
        User user = userRepository.findByUsername(username);
        logger.debug("user::{}", user);
        GamePageRefreshMessage message = new GamePageRefreshMessage(resource.uuid(),
                                                                    username,
                                                                    "User dropped out...",
                                                                    new ArrayList<>(userRepository.findAll()),
                                                                    new ArrayList<>(gameRepository.findAll()));
        resource.getBroadcaster().broadcast(new JacksonEncoder().encode(message));
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
