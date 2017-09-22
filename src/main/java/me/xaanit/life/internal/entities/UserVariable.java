package me.xaanit.life.internal.entities;

import me.xaanit.life.internal.entities.enums.ParameterType;

public class UserVariable extends Variable {

	private final String name;

	public UserVariable(ParameterType type, String info, String name) {
		super(type, info);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
