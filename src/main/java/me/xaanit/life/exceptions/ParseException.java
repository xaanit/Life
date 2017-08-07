package me.xaanit.life.exceptions;

public class ParseException extends RuntimeException {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, String... replace) {
        this(String.format(msg, replace));
    }

}
