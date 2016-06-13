package com.caner.mirzabey.interview.backbase.mancala.draft.ws;

import com.caner.mirzabey.interview.backbase.mancala.draft.config.json.JsonEncoder;
import com.caner.mirzabey.interview.backbase.mancala.draft.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.draft.user.User;
import com.caner.mirzabey.interview.backbase.mancala.draft.user.UserRepository;
import com.caner.mirzabey.interview.backbase.mancala.sample.multichat.JacksonEncoder;

import org.atmosphere.config.service.*;
import org.atmosphere.config.service.DeliverTo.DELIVER_TO;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.caner.mirzabey.interview.backbase.mancala.draft.ws.GameService.PATH;

/**
 * Created by ecanmir on 11.06.2016.
 */
@ManagedService (path = PATH, broadcaster = DefaultBroadcaster.class)
public class GameService {
    public static final Logger logger = LoggerFactory.getLogger(GameService.class);

    public static final String PATH                       = "/game/{name: [a-zA-Z][a-zA-Z_0-9]*}";
    public static final String CONTEXT_PATH               = "/game";
    public static final String USERNAME_REQUEST_PARAMETER = "username";
    public static final String LOGIN_PATH                 = "/game/all";

    @PathParam
    private String gameName;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    public static boolean isValidBroadcaster(Broadcaster broadcaster) {
        if (!PATH.equals(broadcaster.getID()) || !LOGIN_PATH.equals(broadcaster.getID())) {
            return true;
        }
        return false;
    }

    @Ready (encoders = {JsonEncoder.class})
    @DeliverTo (DELIVER_TO.ALL)
    public GameMessage onReady(AtmosphereResource resource) {
        this.logger.info("Connected uuid::{}", resource.uuid());
        String username = resource.getRequest().getParameter("username");
        return new GameMessage(new User(resource.uuid(), username),
                               "Greetings...",
                               new ArrayList<>(userRepository.findAll()),
                               new ArrayList<>(gameRepository.findAll()));
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        String username = event.getResource().getRequest().getParameter(USERNAME_REQUEST_PARAMETER);
        logger.debug("Connection dropped uuid::{}, username::{}", event.getResource().uuid(), username);
        event.broadcaster().broadcast(new JsonEncoder()
                                              .encode(new GameMessage(new User(event.getResource().uuid(), username),
                                                                      "User dropped out...",
                                                                      new ArrayList<>(userRepository.findAll()),
                                                                      new ArrayList<>(gameRepository.findAll()))));
    }

    @Resume
    public void onResume(AtmosphereResource resource) {
        logger.debug("mmmmm=AtmosphereResource::{}", resource.uuid());
        String username = resource.getRequest().getParameter("username");
        logger.debug("mmmmm=username::{}", username);
        User user = userRepository.findByName(username);
        logger.debug("mmmmm=user::{}", user);
        resource.getBroadcaster().broadcast(new GameMessage(user,
                                                            "One user dropped out...",
                                                            new ArrayList<>(userRepository.findAll()),
                                                            new ArrayList<>(gameRepository.findAll())));
    }

    @Message (encoders = JacksonEncoder.class, decoders = GameCommandDecoder.class)
    public GameCommand onGameCommand(GameCommand gameCommand) {
        return gameCommand;
    }

}
