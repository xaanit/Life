package me.xaanit.life.internal.exceptions;

public class TokeniserException extends LifeException {

	public TokeniserException(String msg) {
		super(msg);
	}

	public TokeniserException(String msg, String... objects) {
		this(String.format(msg, objects));
	}
}
