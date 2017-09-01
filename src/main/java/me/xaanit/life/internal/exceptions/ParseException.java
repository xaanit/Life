package me.xaanit.life.internal.exceptions;

public class ParseException extends LifeException {

    public ParseException(String msg, int i) {
        super("[LINE NUMBER: " + i + "] " + msg);
    }

    public ParseException(String msg, int i, String... replace) {
        this(String.format(msg, replace), i);
    }

}
