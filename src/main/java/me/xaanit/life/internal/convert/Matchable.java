package me.xaanit.life.internal.convert;

import me.xaanit.life.internal.entities.token.Token;

public interface Matchable<T> {

	/**
	 * Matches the token.
	 *
	 * @param input The input to match
	 * @param lineNumber The line number
	 * @param parent The parent token (For relationships)
	 * @return Possibly-null {@link Token}
	 * @throws me.xaanit.life.internal.exceptions.TokeniserException If tokenising goes wrong
	 */
	Token<T> convert(String input, int lineNumber, Token parent);

	/**
	 * Matches the token.
	 *
	 * @param input The input to match
	 * @param lineNumber The line number
	 * @return Possibly-null {@link Token}
	 * @throws me.xaanit.life.internal.exceptions.TokeniserException If tokenising goes wrong
	 */
	default Token<T> convert(String input, int lineNumber) {
		return convert(input, lineNumber, null);
	}
}
