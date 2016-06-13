package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.caner.mirzabey.interview.backbase.mancala.game.Game;
import com.caner.mirzabey.interview.backbase.mancala.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.user.User;
import com.caner.mirzabey.interview.backbase.mancala.user.UserRepository;

import org.atmosphere.config.service.BroadcasterListenerService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterListener;
import org.atmosphere.cpr.Deliver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ecanmir on 12.06.2016.
 */
@BroadcasterListenerService
public class GameServiceBroadCasterListener implements BroadcasterListener {
    public static final Logger logger = LoggerFactory.getLogger(GameServiceBroadCasterListener.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onPostCreate(Broadcaster b) {
        logger.debug("BroadcasterListener.onPostCreate::" + b.getID());
        if (GameService.isValidBroadcaster(b)) {
            gameRepository.insert(new Game(b.getID(), null));
        }
    }

    @Override
    public void onComplete(Broadcaster b) {
        logger.debug("BroadcasterListener.onComplete::" + b.getID());
    }

    @Override
    public void onPreDestroy(Broadcaster b) {
        logger.debug("BroadcasterListener.onPreDestroy::" + b.getID());
        gameRepository.remove(b.getID());
    }

    @Override
    public void onAddAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onAddAtmosphereResource --> Broadcaster::{}, AtmosphereResource::{}",
                     b.getID(),
                     r.toString());
        if (b.getID().startsWith(GameService.CONTEXT_PATH)) {
            String username = r.getRequest().getParameter(GameService.USERNAME_REQUEST_PARAMETER);
            userRepository.insert(new User(r.uuid(), username));
        }
    }

    @Override
    public void onRemoveAtmosphereResource(Broadcaster b, AtmosphereResource r) {

        logger.debug("BroadcasterListener.onRemoveAtmosphereResource --> Broadcaster::" + b.getID() + ", resource::" +
                     r.toString());
        if (b.getID().startsWith(GameService.CONTEXT_PATH)) {
            userRepository.remove(r.uuid());
        }
    }

    @Override
    public void onMessage(Broadcaster b, Deliver deliver) {
        logger.debug(
                "BroadcasterListener.onMessage --> Broadcaster::" + b.getID() + ", Deliver::" + deliver.toString());
    }

}
