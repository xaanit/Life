package me.xaanit.life.internal.exceptions;

public class LifeException extends RuntimeException {

	public LifeException(String msg) {
		super(msg);
	}

	public LifeException(String msg, String... objects) {
		this(String.format(msg, objects));
	}

}
