package com.caner.mirzabey.kalah.web.socket.message;

/**
 * Created by ecanmir on 16.06.2016.
 */
public class GameMessage {
    private final long timestamp = System.currentTimeMillis();
    private String message;
    private Object object;
    private Event  event;

    public GameMessage(String message, Object object, Event event) {
        this.message = message;
        this.object = object;
        this.event = event;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public enum Event {
        CUSTOM,
        LOGIN,
        USERS_UPDATE,
        GAMES_UPDATE,
        GAME_MESSAGE,
        GAME_COMMAND
    }

    @Override
    public String toString() {
        return "GameMessage{" +
               "timestamp=" + timestamp +
               ", message='" + message + '\'' +
               ", object=" + object +
               ", event=" + event +
               '}';
    }

}
