package me.xaanit.life.internal.entities;

import me.xaanit.life.internal.entities.LifeMethod.MethodVariable;

public class UserMethod {

	private final MethodVariable[] parameters;
	private final Type returnType;
	private final String body;
	private final String name;
	private final boolean isMain;


	private UserMethod(final MethodVariable[] parameters, final Type returnType, final String body,
			final String name, final boolean isMain) {
		this.parameters = parameters;
		this.returnType = returnType;
		this.body = body;
		this.name = name;
		this.isMain = isMain;
	}

	public UserMethod(final MethodVariable[] parameters, final Type returnType, final String body,
			final String name) {
		this.parameters = parameters;
		this.returnType = returnType;
		this.body = body;
		this.name = name;
		this.isMain = false;
	}

	public UserMethod(String body) {
		this(new MethodVariable[0], Type.VOID, body, "main", true);
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
