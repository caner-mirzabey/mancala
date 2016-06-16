package com.caner.mirzabey.kalah.web.socket.message;

/**
 * Created by ecanmir on 14.06.2016.
 */
public class GameAction {
    private String gameName;
    private String userUUID;
    private String username;
    private int    move;
    private String message;

    public GameAction() {
    }

    public GameAction(String gameName, String userUUID, String username, int move, String message) {
        this.gameName = gameName;
        this.userUUID = userUUID;
        this.username = username;
        this.move = move;
        this.message = message;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
