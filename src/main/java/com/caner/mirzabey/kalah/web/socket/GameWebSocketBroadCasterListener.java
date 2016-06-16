package com.caner.mirzabey.kalah.web.socket;

import com.caner.mirzabey.kalah.game.GameRepository;
import com.caner.mirzabey.kalah.game.data.Game;
import com.caner.mirzabey.kalah.game.exception.GameException;
import com.caner.mirzabey.kalah.user.User;
import com.caner.mirzabey.kalah.user.UserRepository;
import com.caner.mirzabey.kalah.web.socket.config.util.JacksonEncoder;
import com.caner.mirzabey.kalah.web.socket.message.GameMessage;
import com.caner.mirzabey.kalah.web.socket.message.GameMessage.Event;

import org.atmosphere.config.service.BroadcasterListenerService;
import org.atmosphere.cpr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

import static com.caner.mirzabey.kalah.web.socket.GameWebSocket.*;

/**
 * Created by ecanmir on 12.06.2016.
 */
@BroadcasterListenerService
public class GameWebSocketBroadCasterListener implements BroadcasterListener {
    public static final Logger logger = LoggerFactory.getLogger(GameWebSocketBroadCasterListener.class);

    @Autowired
    private AtmosphereConfig atmosphereConfig;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JacksonEncoder jacksonEncoder;

    @Override
    public void onPostCreate(Broadcaster b) {
        logger.debug("BroadcasterListener.onPostCreate::" + b.getID());
        if (isNewGame(b.getID())) {
            String gameName = null;
            try {
                gameName = getGameNameFromBroadcasterID(b.getID());
                gameRepository.insert(new Game(gameName));
                broadcast(new GameMessage("New game created", gameRepository.findAll(), Event.GAMES_UPDATE));
            }
            catch (GameException e) {
                logger.error("Game name parsing error", e);
            }
        }
    }

    @Override
    public void onComplete(Broadcaster b) {
        logger.debug("BroadcasterListener.onComplete::" + b.getID());
    }

    @Override
    public void onPreDestroy(Broadcaster b) {
        logger.debug("BroadcasterListener.onPreDestroy::" + b.getID());
        if (isNewGame(b.getID())) {
            String gameName = null;
            try {
                gameName = getGameNameFromBroadcasterID(b.getID());
                gameRepository.remove(gameName);
                broadcast(new GameMessage("Game dropped", gameRepository.findAll(), Event.GAMES_UPDATE));
            }
            catch (GameException e) {
                logger.error("Game name parsing error", e);
            }
        }
    }

    @Override
    public void onAddAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onAddAtmosphereResource --> Broadcaster::{}, AtmosphereResource::{}",
                     b.getID(),
                     r.toString());
        if (isLoginURL(b.getID())) {
            String username = r.getRequest().getParameter(USERNAME_REQUEST_PARAMETER);
            User   user     = new User(r.uuid(), username);
            userRepository.insert(user);
            r.getBroadcaster().broadcast(jacksonEncoder.encode(new GameMessage("Greetings...",
                                                                               gameRepository.findAll(),
                                                                               Event.LOGIN)), r);
            broadcast(new GameMessage("User logged in", userRepository.findAll(), Event.USERS_UPDATE));
        }

    }

    @Override
    public void onRemoveAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onRemoveAtmosphereResource --> Broadcaster::" + b.getID() + ", resource::" +
                     r.toString());
        if (isLoginURL(b.getID())) {
            userRepository.remove(r.uuid());
            broadcast(new GameMessage("User disconnected in", userRepository.findAll(), Event.USERS_UPDATE));
        }
    }

    @Override
    public void onMessage(Broadcaster b, Deliver deliver) {
        logger.debug("BroadcasterListener.onMessage --> Broadcaster::{}, Deliver::{}", b.getID(), deliver.toString());
    }

    public static boolean isNewGame(String broadcastID) {
        if (broadcastID.startsWith(CONTEXT_PATH) && !broadcastID.equals(CONTEXT_PATH) && !PATH.equals(broadcastID) &&
            !LOGIN_PATH.equals(broadcastID)) {
            return true;
        }
        return false;
    }

    public static boolean isLoginURL(String broadcastID) {
        if (broadcastID.equals(LOGIN_PATH)) {
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

    private void broadcast(Object o) {
        String json = jacksonEncoder.encode(o);
        atmosphereConfig.metaBroadcaster().broadcastTo(LOGIN_PATH, json, true);
    }

    private void schedule(Object o) {
        String json = jacksonEncoder.encode(o);
        atmosphereConfig.metaBroadcaster().scheduleTo(LOGIN_PATH, json, 1000, TimeUnit.MILLISECONDS);
    }

    private void sendPrivate(AtmosphereResource resource, Object o) {
        String json = jacksonEncoder.encode(o);
        Broadcaster privateChannel = atmosphereConfig.getBroadcasterFactory().lookup(
                CONTEXT_PATH + "/" + resource.uuid());
        privateChannel.addAtmosphereResource(resource);
        privateChannel.delayBroadcast(json, 1000, TimeUnit.MILLISECONDS);
    }
}
