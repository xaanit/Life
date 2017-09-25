package me.xaanit.life.internal.entities;

import me.xaanit.life.internal.entities.LifeMethod.MethodVariable;

public class UserMethod {

	private final MethodVariable[] parameters;
	private final Type returnType;
	private final String body;
	private final String name;


	public UserMethod(final MethodVariable[] parameters, final Type returnType, final String body, final String name) {
		this.parameters = parameters;
		this.returnType = returnType;
		this.body = body;
		this.name = name;
	}

	public MethodVariable[] getParameters() {
		return parameters;
	}

	public Type getReturnType() {
		return returnType;
	}

	public String getBody() {
		return body;
	}

	public String getName() {
		return name;
	}
}
