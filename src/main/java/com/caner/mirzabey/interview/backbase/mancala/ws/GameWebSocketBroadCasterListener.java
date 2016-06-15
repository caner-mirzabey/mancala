package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.caner.mirzabey.interview.backbase.mancala.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.caner.mirzabey.interview.backbase.mancala.game.exception.GameException;
import com.caner.mirzabey.interview.backbase.mancala.user.UserRepository;

import org.atmosphere.config.service.BroadcasterListenerService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterListener;
import org.atmosphere.cpr.Deliver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.caner.mirzabey.interview.backbase.mancala.ws.GameWebSocket.getGameNameFromBroadcasterID;

/**
 * Created by ecanmir on 12.06.2016.
 */
@BroadcasterListenerService
public class GameWebSocketBroadCasterListener implements BroadcasterListener {
    public static final Logger logger = LoggerFactory.getLogger(GameWebSocketBroadCasterListener.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onPostCreate(Broadcaster b) {
        logger.debug("BroadcasterListener.onPostCreate::" + b.getID());
        if (GameWebSocket.isValidBroadcaster(b)) {
            String gameName = null;
            try {
                gameName = getGameNameFromBroadcasterID(b.getID());
                gameRepository.insert(new Game(gameName));
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
        if (GameWebSocket.isValidBroadcaster(b)) {
            String gameName = null;
            try {
                gameName = getGameNameFromBroadcasterID(b.getID());
                gameRepository.remove(gameName);
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
    }

    @Override
    public void onRemoveAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onRemoveAtmosphereResource --> Broadcaster::" + b.getID() + ", resource::" +
                     r.toString());
    }

    @Override
    public void onMessage(Broadcaster b, Deliver deliver) {
        logger.debug("BroadcasterListener.onMessage --> Broadcaster::{}, Deliver::{}", b.getID(), deliver.toString());
    }

}
