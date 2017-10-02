package me.xaanit.life.internal.matching;

import me.xaanit.life.internal.entities.token.Tokeniser;

public abstract class Tokenisable {

	private final Tokeniser tokeniser;


	public Tokenisable(final Tokeniser tokeniser) {
		this.tokeniser = tokeniser;
	}

	public Tokeniser getTokeniser() {
		return tokeniser;
	}

	public static String trim(String input) {
		if(input.isEmpty()) {
			return input;
		}
		while(Character.isWhitespace(input.charAt(0)) || input.charAt(0) == '\n'|| input.charAt(0) == '\r') {
			input = input.substring(1);
		}
		input = input.trim();
		return input;
	}
}
