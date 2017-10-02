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
		return "MethodVariable{\n\ntype=" + type + "\n\nname=" + name + "\n\n}";
	}
}
