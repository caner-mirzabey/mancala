package com.caner.mirzabey.interview.backbase.mancala.ws;

/**
 * Created by ecanmir on 13.06.2016.
 */
public class GameCommand {
    //Store number
    private int    move;
    private String username;
    private String message;

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
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

    @Override
    public String toString() {
        return "GameCommand{" +
               "move=" + move +
               ", username='" + username + '\'' +
               ", message='" + message + '\'' +
               '}';
    }

}
