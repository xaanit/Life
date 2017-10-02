package me.xaanit.life.internal.exceptions;

public class TokeniserException extends LifeException {

	public TokeniserException(String msg, String... objects) {
		super(String.format(msg, objects));
	}
}
