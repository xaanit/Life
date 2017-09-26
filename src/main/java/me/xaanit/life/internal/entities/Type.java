package me.xaanit.life.internal.entities;

public enum Type {
	STRING("String"),
	DOUBLE,
	CHAR,
	FLOAT,
	LONG,
	INT,
	BOOLEAN,
	VOID;


	private final String name;

	Type(final String name) {
		this.name = name;
	}

	Type() {
		this.name = toString().toLowerCase();
	}

	public String getName() {
		return this.name;
	}
}
