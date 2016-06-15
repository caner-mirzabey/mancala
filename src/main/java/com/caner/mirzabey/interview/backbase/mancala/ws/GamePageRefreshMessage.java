package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.caner.mirzabey.interview.backbase.mancala.game.data.Game;
import com.caner.mirzabey.interview.backbase.mancala.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecanmir on 11.06.2016.
 */
public class GamePageRefreshMessage {
    private final long time = System.currentTimeMillis();
    private String uuid;
    private String username;
    private String message;
    private List<User> users = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    public GamePageRefreshMessage(String uuid, String username, String message) {
        this.uuid = uuid;
        this.username = username;
        this.message = message;
    }

    public GamePageRefreshMessage(String uuid, String username, String message, List<User> users, List<Game> games) {
        this.uuid = uuid;
        this.username = username;
        this.message = message;
        this.users = users;
        this.games = games;
    }

    public long getTime() {
        return time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "GamePageRefreshMessage{" +
               "time=" + time +
               ", username='" + username + '\'' +
               ", message='" + message + '\'' +
               ", users=" + users +
               ", games=" + games +
               '}';
    }
}
