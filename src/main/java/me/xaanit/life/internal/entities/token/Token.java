package me.xaanit.life.internal.entities.token;

/**
 * Represents a token found by the tokeniser.
 */
public class Token<T> {

	private final String line;
	private final TokenType type;
	private final Token parent;
	private final T info;

	public Token(final String line, final TokenType type, final T info) {
		this.line = line;
		this.type = type;
		this.parent = null;
		this.info = info;
	}


	/**
	 * Gets the line (i.e {@code if(x == 4)}
	 *
	 * @return The line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * Gets what type the token is (method, variable, etc...)
	 *
	 * @return The type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Gets the parent token. I.e a method
	 *
	 * @return The parent
	 */
	public Token getParent() {
		return parent;
	}

	/**
	 * Gets the info (i.e token of {@code const int age = 5;} returns a {@link
	 * me.xaanit.life.internal.entities.UserVariable} with info {@code 4}, name {@code age}, type
	 * {@code ParameterType.INT}, and isConstant {@code true}
	 *
	 * @return The
	 */
	public T getInfo() {
		return info;
	}


	@Override
	public String toString() {
		return "Token{" +
				"line='" + line + '\'' +
				", type=" + type +
				", parent=" + parent +
				", info=" + info +
				'}';
	}
}
