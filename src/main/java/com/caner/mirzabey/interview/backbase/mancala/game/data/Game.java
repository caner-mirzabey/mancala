package com.caner.mirzabey.interview.backbase.mancala.game.data;

import com.caner.mirzabey.interview.backbase.mancala.user.User;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by ecanmir on 11.06.2016.
 */
public class Game {
    private final long   timestamp = System.currentTimeMillis();
    private final String uuid      = UUID.randomUUID().toString();
    private final String name;
    private       Board  board;

    private Status status;

    public Game(String name) {
        this.name = name;
        this.status = Status.WAITING_FOR_ANOTHER_PLAYER;
    }

    public Game(String name, int seedCount) {
        this(name);
        this.board = new Board(seedCount);
    }

    public Game(String name, int seedCount, User player1, User player2) {
        this(name);
        this.board = new Board(seedCount, player1, player2);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        WAITING_FOR_ANOTHER_PLAYER,
        WAITING_FOR_START,
    }

    @Override
    public String toString() {
        return "Game{" +
               "timestamp=" + timestamp +
               ", uuid='" + uuid + '\'' +
               ", name='" + name + '\'' +
               ", board=" + board +
               ", status=" + status +
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
        return getTimestamp() == game.getTimestamp() &&
               Objects.equals(getUuid(), game.getUuid()) &&
               Objects.equals(getName(), game.getName()) &&
               Objects.equals(getBoard(), game.getBoard()) &&
               getStatus() == game.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getUuid(), getName(), getBoard(), getStatus());
    }
}
