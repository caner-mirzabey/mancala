package com.caner.mirzabey.interview.backbase.mancala.ws;

import com.caner.mirzabey.interview.backbase.mancala.game.Game;
import com.caner.mirzabey.interview.backbase.mancala.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecanmir on 11.06.2016.
 */
public class GameMessage {
    private final long time = System.currentTimeMillis();
    private final User user;

    private String message;
    private List<User> users = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    public GameMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public GameMessage(User user, String message, List<User> users, List<Game> games) {
        this.user = user;
        this.message = message;
        this.users = users;
        this.games = games;
    }

    public long getTime() {
        return time;
    }

    public User getUser() {
        return user;
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
        return "GameMessage{" +
               "time=" + time +
               ", user=" + user +
               ", message='" + message + '\'' +
               ", user=" + users +
               ", games=" + games +
               '}';
    }
}
