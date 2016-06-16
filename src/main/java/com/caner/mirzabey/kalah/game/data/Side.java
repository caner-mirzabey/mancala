package com.caner.mirzabey.kalah.game.data;

import com.caner.mirzabey.kalah.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Created by ecanmir on 14.06.2016.
 */
public class Side {
    public static final int HOUSE_COUNT = 6;

    private User    owner;
    private boolean turnOnMe;

    private Stack<Seed>       kalah = new Stack<>();
    private List<Stack<Seed>> pit   = new ArrayList<>(HOUSE_COUNT);

    public Side(int seedCount) {
        init(seedCount);
    }

    public Side(User owner, int seedCount) {
        init(seedCount);
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    private void init(int seedCount) {
        for (int i = 1; i <= HOUSE_COUNT; i++) {
            Stack<Seed> seeds = new Stack<>();
            for (int j = 1; j <= seedCount; j++) {
                seeds.push(new Seed((i * 10) + j));
            }
            pit.add(seeds);
        }
    }

    public void setKalah(Stack<Seed> kalah) {
        this.kalah = kalah;
    }

    public void setPit(List<Stack<Seed>> pit) {
        this.pit = pit;
    }

    public List<Stack<Seed>> getPit() {
        return pit;
    }

    public Stack<Seed> getKalah() {
        return kalah;
    }

    public boolean isTurnOnMe() {
        return turnOnMe;
    }

    public void setTurnOnMe(boolean turnOnMe) {
        this.turnOnMe = turnOnMe;
    }

    @Override
    public String toString() {
        return "Side{" +
               "owner=" + owner +
               ", turnOnMe=" + turnOnMe +
               ", kalah=" + kalah +
               ", pit=" + pit +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Side)) {
            return false;
        }
        Side side = (Side) o;
        return isTurnOnMe() == side.isTurnOnMe() &&
               Objects.equals(getOwner(), side.getOwner()) &&
               Objects.equals(getKalah(), side.getKalah()) &&
               Objects.equals(getPit(), side.getPit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), isTurnOnMe(), getKalah(), getPit());
    }
}