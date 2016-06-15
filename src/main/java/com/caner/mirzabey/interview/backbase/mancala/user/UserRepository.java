package com.caner.mirzabey.interview.backbase.mancala.user;

import com.caner.mirzabey.interview.backbase.mancala.ws.GameWebSocket;

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
public class UserRepository {
    public static final Logger logger = LoggerFactory.getLogger(GameWebSocket.class);

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User find(String uuid) {
        return users.get(uuid);
    }

    public User findByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean insert(User user) {
        if (users.containsKey(user.getUuid()) && !StringUtils.isEmpty(user.getUuid()) &&
            !StringUtils.isEmpty(user.getUsername())) {
            return false;
        }
        users.put(user.getUuid(), user);
        logger.debug("User added::" + user);
        return true;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User remove(User user) {
        user = users.remove(user.getUuid());
        logger.debug("User removed::" + user);
        return user;
    }

    public User remove(String uuid) {
        User user = users.remove(uuid);
        logger.debug("User removed::" + user);
        return user;
    }

}
