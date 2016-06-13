package com.caner.mirzabey.interview.backbase.mancala.draft.game;

import com.caner.mirzabey.interview.backbase.mancala.draft.user.User;

import java.util.Objects;

/**
 * Created by ecanmir on 11.06.2016.
 */
public class Game {
    private final long timesamp = System.currentTimeMillis();
    private final String name;
    private final User   owner;
    private       User   opponent;

    public Game(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public long getTimesamp() {
        return timesamp;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    @Override
    public String toString() {
        return "Game{" +
               "timesamp=" + timesamp +
               ", name='" + name + '\'' +
               ", owner=" + owner +
               ", opponent=" + opponent +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        Game game = (Game) o;
        return getTimesamp() == game.getTimesamp() &&
               Objects.equals(getName(), game.getName()) &&
               Objects.equals(getOwner(), game.getOwner()) &&
               Objects.equals(getOpponent(), game.getOpponent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimesamp(), getName(), getOwner(), getOpponent());
    }
}
