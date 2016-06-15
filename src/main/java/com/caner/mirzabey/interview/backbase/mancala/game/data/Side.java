package com.caner.mirzabey.interview.backbase.mancala.game.data;

import com.caner.mirzabey.interview.backbase.mancala.user.User;

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

    private Stack<Seed>       store  = new Stack<>();
    private List<Stack<Seed>> houses = new ArrayList<>(HOUSE_COUNT);

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
            houses.add(seeds);
        }
    }

    public void setStore(Stack<Seed> store) {
        this.store = store;
    }

    public void setHouses(List<Stack<Seed>> houses) {
        this.houses = houses;
    }

    public List<Stack<Seed>> getHouses() {
        return houses;
    }

    public Stack<Seed> getStore() {
        return store;
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
               ", store=" + store +
               ", houses=" + houses +
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
               Objects.equals(getStore(), side.getStore()) &&
               Objects.equals(getHouses(), side.getHouses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), isTurnOnMe(), getStore(), getHouses());
    }
}