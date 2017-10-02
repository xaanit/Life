package me.xaanit.life.internal.entities;

public class MethodVariable {

	private final Type type;
	private final String name;

	public MethodVariable(final Type type) {
		this(type, "");
	}

	public MethodVariable(final Type type, final String name) {
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "MethodVariable{" +
				"type=" + type +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof MethodVariable)) {
			return false;
		}

		MethodVariable that = (MethodVariable) o;

		if(type != that.type) {
			return false;
		}
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = type.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}
}
