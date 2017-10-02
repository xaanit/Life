package me.xaanit.life.internal.entities;

/**
 * Represents a user made variable
 */
public class UserVariable extends Variable {

	private final String name;
	private final boolean constant;
	private final boolean isGlobal;

	/**
	 * Makes a new User Variable
	 *
	 * @param type The type {@link Type}
	 * @param info The info (i.e if type == {@link Type}.INT, this could be "4")
	 * @param name The name to call it by
	 * @param constant If the variable is final or not
	 */
	public UserVariable(final Type type, final String info, final String name,
			final boolean constant, final boolean isGlobal) {
		super(type, info);
		this.name = name;
		this.constant = constant;
		this.isGlobal = isGlobal;
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
		return isGlobal;
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

		if(constant != that.constant) {
			return false;
		}
		if(isGlobal != that.isGlobal) {
			return false;
		}
		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (constant ? 1 : 0);
		result = 31 * result + (isGlobal ? 1 : 0);
		return result;
	}
}
