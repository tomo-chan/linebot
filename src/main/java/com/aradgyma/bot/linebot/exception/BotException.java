package com.aradgyma.bot.linebot.exception;

public class BotException extends Exception {

    public BotException() {
        super();
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable e) {
        super(message, e);
    }

    public BotException(Throwable e) {
        super(e);
    }
}
