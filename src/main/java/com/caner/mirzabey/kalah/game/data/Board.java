package com.caner.mirzabey.kalah.game.data;

import com.caner.mirzabey.kalah.user.User;

import java.util.Objects;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class Board {
    private final int  seedCount;
    private Side side1;
    private Side side2;

    public Board(int seedCount) {
        this.seedCount = seedCount;
        this.side1 = new Side(seedCount);
        this.side2 = new Side(seedCount);
    }

    public Board(int seedCount, User user1, User user2) {
        this.seedCount = seedCount;
        this.side1 = new Side(user1, seedCount);
        this.side2 = new Side(user2, seedCount);
    }

    public int getSeedCount() {
        return seedCount;
    }

    public Side getSide1() {
        return side1;
    }

    public Side getSide2() {
        return side2;
    }

    public Side getPlayerSideByUserUUID(String userUUID) {
        if (side1.getOwner().getUuid().equals(userUUID)) {
            return side1;
        }
        else if (side2.getOwner().getUuid().equals(userUUID)) {
            return side2;
        }
        return null;
    }

    public Side getPlayerSideByUsername(String username) {
        if (side1.getOwner().getUsername().equals(username)) {
            return side1;
        }
        else if (side2.getOwner().getUsername().equals(username)) {
            return side2;
        }
        return null;
    }

    public Side getOpponentSideByUserUUID(String userUUID) {
        if (side1.getOwner().getUuid().equals(userUUID)) {
            return side2;
        }
        else if (side2.getOwner().getUuid().equals(userUUID)) {
            return side1;
        }
        return null;
    }

    public Side getOpponentSideByUsername(String username) {
        if (side1.getOwner().getUsername().equals(username)) {
            return side2;
        }
        else if (side2.getOwner().getUsername().equals(username)) {
            return side1;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Board{" +
               "seedCount=" + seedCount +
               ", side1=" + side1 +
               ", side2=" + side2 +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        return getSeedCount() == board.getSeedCount() &&
               Objects.equals(getSide1(), board.getSide1()) &&
               Objects.equals(getSide2(), board.getSide2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeedCount(), getSide1(), getSide2());
    }
}
