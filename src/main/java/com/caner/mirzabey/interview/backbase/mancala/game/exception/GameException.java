package com.caner.mirzabey.interview.backbase.mancala.game.exception;

/**
 * Created by ecanmir on 15.06.2016.
 */
public class GameException extends Exception {
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(Throwable cause) {
        super(cause);
    }

    public GameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
