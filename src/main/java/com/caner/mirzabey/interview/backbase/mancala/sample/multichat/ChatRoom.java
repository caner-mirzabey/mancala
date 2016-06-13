/*
 * Copyright 2016 Async-IO.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.caner.mirzabey.interview.backbase.mancala.sample.multichat;

import com.caner.mirzabey.interview.backbase.mancala.draft.config.json.JsonEncoder;
import com.caner.mirzabey.interview.backbase.mancala.draft.game.Game;
import com.caner.mirzabey.interview.backbase.mancala.draft.game.GameRepository;
import com.caner.mirzabey.interview.backbase.mancala.draft.user.User;
import com.caner.mirzabey.interview.backbase.mancala.draft.user.UserRepository;
import com.caner.mirzabey.interview.backbase.mancala.draft.ws.GameMessage;

import org.atmosphere.config.service.*;
import org.atmosphere.config.service.Message;
import org.atmosphere.cpr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple annotated class that demonstrate the power of Atmosphere. This class supports all transports, support
 * message length guarantee, heart beat, message cache thanks to the {@link ManagedService}.
 */
@ManagedService (path = "/chat-room/{rooms: [a-zA-Z][a-zA-Z_0-9]*}")
public class ChatRoom {
    public static final  String                            PATH   = "/chat-room/";
    private static final Logger                            logger = LoggerFactory.getLogger(ChatRoom.class);
    private final        ConcurrentHashMap<String, String> users  = new ConcurrentHashMap<String, String>();

    @PathParam ("rooms")
    private String chatroomName;

    @Inject
    private BroadcasterFactory factory;

    @Autowired
    private AtmosphereResourceFactory resourceFactory;

    @Autowired
    private MetaBroadcaster metaBroadcaster;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AtmosphereConfig atmosphereConfig;

    private static Collection<String> getRooms(Collection<Broadcaster> broadcasters) {
        Collection<String> result = new ArrayList<String>();
        for (Broadcaster broadcaster : broadcasters) {
            if (!("/*".equals(broadcaster.getID()))) {
                // if no rooms is specified, use ''
                String[] p = broadcaster.getID().split("/");
                result.add(p.length > 2 ? p[2] : "");
            }
        }
        return result;
    }

    /**
     * Invoked when the connection as been fully established and suspended, e.g ready for receiving messages.
     *
     * @param r
     */
    @Ready (encoders = {JacksonEncoder.class})
    @DeliverTo (DeliverTo.DELIVER_TO.ALL)
    public ChatProtocol onReady(final AtmosphereResource r) {
        logger.info("Browser {} loggedIn.", r.uuid());
        BroadcasterFactory factory = r.getAtmosphereConfig().getBroadcasterFactory();

        logger.debug("broadcasters::" + String.valueOf(factory.lookupAll().size()));
        for (Broadcaster broadcaster : factory.lookupAll()) {
            logger.debug("broadcaster::" + broadcaster.getID());
            for (AtmosphereResource atmosphereResource : broadcaster.getAtmosphereResources()) {
                logger.debug(atmosphereResource.uuid() + ", " + atmosphereResource);
                atmosphereResource.resume();
                atmosphereResource.write(new JsonEncoder()
                                                 .encode(new GameMessage(
                                                         null,
                                                         "",
                                                         new ArrayList<User>(userRepository.findAll()),
                                                         new ArrayList<Game>(gameRepository.findAll()))));
                atmosphereResource.resume();
            }
        }
        return new ChatProtocol(users.keySet(), getRooms(factory.lookupAll()));
    }

    /**
     * Invoked when the client disconnect or when an unexpected closing of the underlying connection happens.
     *
     * @param event
     */
    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        if (event.isCancelled()) {
            // We didn't get notified, so we remove the user.
            users.values().remove(event.getResource().uuid());
            logger.info("Browser {} unexpectedly disconnected", event.getResource().uuid());
        }
        else if (event.isClosedByClient()) {
            logger.info("Browser {} closed the connection", event.getResource().uuid());
        }
    }

    /**
     * Simple annotated class that demonstrate how {@link org.atmosphere.config.managed.Encoder} and {@link org.atmosphere.config.managed.Decoder
     * can be used.
     *
     * @param message an instance of {@link ChatProtocol }
     * @return
     * @throws IOException
     */
    @Message (encoders = {JacksonEncoder.class}, decoders = {ProtocolDecoder.class})
    public ChatProtocol onMessage(ChatProtocol message) throws IOException {

        if (message.getMessage().contains("disconnecting")) {
            users.remove(message.getAuthor());
            return new ChatProtocol(message.getAuthor(),
                                    " disconnected from room " + chatroomName,
                                    users.keySet(),
                                    getRooms(factory.lookupAll()));
        }

        if (!users.containsKey(message.getAuthor())) {
            users.put(message.getAuthor(), message.getUuid());
            return new ChatProtocol(message.getAuthor(),
                                    " entered room " + chatroomName,
                                    users.keySet(),
                                    getRooms(factory.lookupAll()));
        }

        message.setUsers(users.keySet());
        logger.info("{} just send {}", message.getAuthor(), message.getMessage());
        return new ChatProtocol(message.getAuthor(),
                                message.getMessage(),
                                users.keySet(),
                                getRooms(factory.lookupAll()));
    }

    @Message (decoders = {UserDecoder.class})
    public void onPrivateMessage(UserMessage user) throws IOException {
        String userUUID = users.get(user.getUser());
        if (userUUID != null) {
            // Retrieve the original AtmosphereResource
            AtmosphereResource r = resourceFactory.find(userUUID);

            if (r != null) {
                logger.debug("Private message" + user.getMessage().split(":")[1]);
                BroadcasterFactory factory = r.getAtmosphereConfig().getBroadcasterFactory();
                ChatProtocol m = new ChatProtocol(user.getUser(),
                                                  " sent you a private message: " + user.getMessage().split(":")[1],
                                                  users.keySet(),
                                                  getRooms(factory.lookupAll()));
                if (!user.getUser().equalsIgnoreCase("all")) {
                    logger.debug("broadcast to room::" + chatroomName + " by user::" + user.getUser() + " uuid::" +
                                 userUUID);
                    factory.lookup(PATH + chatroomName).broadcast(m, r);
                }
            }
        }
        else {
            ChatProtocol m = new ChatProtocol(user.getUser(),
                                              " sent a message to all chatroom: " + user.getMessage().split(":")[1],
                                              users.keySet(),
                                              getRooms(factory.lookupAll()));
            metaBroadcaster.broadcastTo("/*", m);
        }
    }

}
