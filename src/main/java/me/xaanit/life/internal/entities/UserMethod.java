package me.xaanit.life.internal.entities;

import java.util.Arrays;

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

	public boolean isMain() {
		return isMain;
	}

	@Override
	public String toString() {
		return "UserMethod{" +
				"parameters=" + Arrays.toString(parameters) +
				", returnType=" + returnType +
				", body='" + body + '\'' +
				", name='" + name + '\'' +
				", isMain=" + isMain +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof UserMethod)) {
			return false;
		}

		UserMethod that = (UserMethod) o;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if(!Arrays.equals(parameters, that.parameters)) {
			return false;
		}
		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(parameters);
		result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
		result = 31 * result + (body != null ? body.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (isMain ? 1 : 0);
		return result;
	}
}
