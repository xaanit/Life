package me.xaanit.life.internal.convert;

import java.util.regex.Pattern;

public enum Regex {

	// Validation, for making sure things are good before figuring out token relationships
	/**
	 * Represents a method declaration. i.e {@code def void main() {}}
	 */
	METHOD_DECLARATION("def\\s+.+\\s+.+\\s*\\(.+\\)\\s*\\{(.|\\R)*}\\R*"),

	/**
	 * Represents an if statement, i.e {@code if(true) {}}
	 */
	IF("if\\s*\\(.+\\)\\s*\\{?(\\R|.)*}\\R*"),

	/**
	 * Represents a for loop, i.e {@code for(0 to 10 by i++) {}}
	 */
	FOR("for\\s*\\([0-9a-zA-Z_]+\\s+to\\s+[0-9a-zA-Z_]+\\s+by\\s[a-zA-Z_+=]+\\)\\s*\\{(\\R|.)*}\\R*"),

	/**
	 * Represents a while loop, i.e {@code while(true) {}}
	 */
	WHILE("while\\s*\\(.+\\)\\s*\\{(\\R|.)*}\\R*"),

	// Grabbing of information
	/**
	 * Represents variable creation by the user, i.e {@code const String author = "xaanit";} or {@code
	 * String project = "Life";}
	 */
	VARIABLE_CREATION("(const\\s+)?(.+)\\s+(.+)\\s+=\\s+(.+);\\R*"),

	/**
	 * Represents variable re-assignment, i.e {@code project = "NotLife";}
	 */
	VARIABLE_REASSIGNMENT("(.+)\\s+=\\s+(.+);\\R*"),

	/**
	 * Calling a method, i.e {@code println("Hello World!");}
	 */
	METHOD_CALL("(.+)\\((.*)\\);\\R*"),

	/**
	 * Grabs information about the method, i.e it's name, return type, and parameters
	 */
	METHOD_INFORMATION("def\\s*([^\\s]+)\\s*(.+)\\((.*)\\)"),

	/**
	 * Grabs information about the if statement, i.e the condition
	 */
	IF_INFORMATION("if\\((.+)\\)"),

	/**
	 * Grabs information about the while loop, i.e the condition
	 */
	WHILE_INFORMATION("while\\((.+)\\)"),

	/**
	 * Gets information about the for loop, i.e the starting number (or variable), the ending number (or variable), and the incrementation
	 */
	FOR_INFORMATION("for\\s*\\(([a-zA-Z_0-9]+)\\s*to\\s*([a-zA-Z_0-9]+)\\s*by\\s*([a-zA-Z_0-9+=\\s]+)");

	private final String regex;

	Regex(final String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return this.regex;
	}

	public Pattern compile() {
		if(!toString().toLowerCase().contains("variable") && !toString().toLowerCase().contains("information") && !toString().equalsIgnoreCase("method_call")) {
			throw new UnsupportedOperationException("You can only compile the patterns of information regex, variable regex, and method call!");
		}
		return Pattern.compile(regex);
	}
}
