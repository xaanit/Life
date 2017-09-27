package me.xaanit.life.internal.convert;

public enum MatcherVariables {
	IDENTIFIER_FIRST("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	IDENTIFIER_SECOND(IDENTIFIER_FIRST.info + "_0123456789"),
	EVERY_CHARACTER(IDENTIFIER_SECOND.info + ";*+=\\{}%&| ()\r\n.\"'-/");



	private final String info;

	MatcherVariables(final String info) {
		this.info = info;
	}

	public String getInfo() {
		return this.info;
	}

	@Override
	public String toString() {
		return getInfo();
	}
}
