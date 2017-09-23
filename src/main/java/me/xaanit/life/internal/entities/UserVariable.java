package me.xaanit.life.internal.entities;

import me.xaanit.life.internal.entities.enums.ParameterType;

/**
 * Represents a user made variable
 */
public class UserVariable extends Variable {

	private final String name;
	private final boolean constant;

	/**
	 * Makes a new User Variable
	 *
	 * @param type The type {@link ParameterType}
	 * @param info The info (i.e if type == {@link ParameterType}.INT, this could be "4")
	 * @param name The name to call it by
	 * @param constant If the variable is final or not
	 */
	public UserVariable(final ParameterType type, final String info, final String name,
			final boolean constant) {
		super(type, info);
		this.name = name;
		this.constant = constant;
	}

	/**
	 * Grabs the name of the variable
	 *
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * If the variable is final or not
	 *
	 * @return {@code true} if the variable is marked final; else false
	 */
	public boolean isConstant() {
		return constant;
	}
}
