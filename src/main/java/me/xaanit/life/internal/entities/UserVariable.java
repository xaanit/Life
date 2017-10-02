package me.xaanit.life.internal.entities;

/**
 * Represents a user made variable
 */
public class UserVariable extends Variable {

	private final String name;
	private final boolean constant;
	private final boolean global;

	/**
	 * Makes a new User Variable
	 *
	 * @param type The type {@link Type}
	 * @param info The info (i.e if type == {@link Type}.INT, this could be "4")
	 * @param name The name to call it by
	 * @param constant If the variable is final or not
	 * @param global if the variable is global or not
	 */
	public UserVariable(final Type type, final String info, final String name,
			final boolean constant, final boolean global) {
		super(type, info);
		this.name = name;
		this.constant = constant;
		this.global = global;
	}

	/**
	 * Makes a new User Variable
	 *
	 * @param type The type {@link Type}
	 * @param info The info (i.e if type == {@link Type}.INT, this could be "4")
	 * @param name The name to call it by
	 * @param constant If the variable is final or not
	 */
	public UserVariable(final Type type, final String info, final String name,
			final boolean constant) {
		this(type, info, name, constant, false);
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

	/**
	 * If the variable is top level or not
	 *
	 * @return If the variable is top level
	 */
	public boolean isGlobal() {
		return global;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof UserVariable)) {
			return false;
		}

		UserVariable that = (UserVariable) o;

		return name.equals(that.name) && getType() == that.getType();
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (constant ? 1 : 0);
		result = 31 * result + (global ? 1 : 0);
		return result;
	}
}
