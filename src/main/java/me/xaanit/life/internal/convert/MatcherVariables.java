package me.xaanit.life.internal.convert;

import java.util.regex.Pattern;

public enum MatcherVariables {
	IDENTIFIER_FIRST("[a-zA-Z]"),
	IDENTIFIER_SECOND(IDENTIFIER_FIRST.info + "[]"),
	METHOD(""),
	VARIABLE("(const\\s+)?(.+)\\s+(.+)\\s+=\\s+(.+);"),
	VARIABLE_EDIT("(.+)\\s+=\\s+(.+);"),
	IF("if\\((.+)\\)"),
	ELIF("el" + IF.info),
	ELSE("else"),
	FOR("for\\((([0-9]+)\\s+to\\s+([0-9]+)\\s+by\\s+(.+))\\)"),
	WHILE("while\\((.+)\\)");


	private final String info;

	MatcherVariables(final String info) {
		this.info = info;
	}

	public String getInfo() {
		return this.info;
	}

	public final Pattern getPattern() {
		return Pattern.compile(info);
	}

	@Override
	public String toString() {
		return getInfo();
	}
}
