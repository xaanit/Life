package me.xaanit.life.internal.entities;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import me.xaanit.life.internal.entities.enums.ParameterType;
import me.xaanit.life.internal.exceptions.LifeException;

/**
 * Represents a {@link Method} annotated by {@link me.xaanit.life.internal.annotations.LifeExecutable}
 */
public class LifeMethod {

	public final MethodVariable[] variables;
	public final Method method;
	public final Class clazz;

	/**
	 * Makes a new LifeMethod
	 *
	 * @param clazz The class it's in
	 * @param method The method itself
	 */
	public LifeMethod(final Class clazz, final Method method) {
		this.clazz = clazz; // We pass in the class just to be sure
		this.method = method;
		final Parameter[] parameters = method.getParameters(); // To loop through
		this.variables = new MethodVariable[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			ParameterType type;
			String info = parameters[i].getType()
					.getName(); // All primitives are just the name, string is java.lang.String
			if(info.equalsIgnoreCase("long")) {
				type = ParameterType.LONG;
			} else if(info.equalsIgnoreCase("double")) {
				type = ParameterType.DOUBLE;
			} else if(info.equalsIgnoreCase("char")) {
				type = ParameterType.CHAR;
			} else if(info.equalsIgnoreCase("float")) {
				type = ParameterType.FLOAT;
			} else if(info.equalsIgnoreCase("int")) {
				type = ParameterType.INT;
			} else if(info.equalsIgnoreCase("boolean")) {
				type = ParameterType.BOOLEAN;
			} else if(info.equalsIgnoreCase("java.lang.String")) {
				type = ParameterType.STRING;
			} else {
				throw new LifeException(
						"Only primitives (excluding byte and short) and Strings are allowed!");
			}
			variables[i] = new MethodVariable(type);
		}

		String returnType = method.getReturnType().getName();
		if(!equalsAny(returnType, "double", "char", "long", "int", "boolean", "float",
				"java.lang.String", "void")) { // If the
			// return type isn't one of these, it's a problem
			throw new LifeException(
					"Methods can only return primitives (excluding byte and shor), Strings, or void!");
		}
	}

	/**
	 * Checks to see if an object is equal to a list of Objects
	 *
	 * @param o The object to check
	 * @param objects The objects to check against
	 * @return {@code true} if it does equal any; otherwise {@code false}
	 */
	private boolean equalsAny(Object o, Object... objects) {
		for(Object object : objects) {
			if(o.equals(object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the variables given are the same as this method
	 *
	 * @param variables The variables to check
	 * @return {@code true} if the variables are the same (type); else false
	 */
	public boolean varCheck(List<Variable> variables) {
		if(variables.size() != this.variables.length) {
			return false;
		}
		for(int i = 0; i < this.variables.length; i++) {
			if(variables.get(i).getType() != this.variables[i].getType()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the {@link Method} attached to this Method
	 *
	 * @return The appropriate {@link Method}
	 */
	public Method getMethod() {
		return method;
	}


	/**
	 * Gets the {@link Class} this method is from
	 *
	 * @return The appropriate {@link Class}
	 */
	public Class getClazz() {
		return clazz;
	}

	@Override
	public String toString() {
		return "LifeMethod{" +
				"variables=" + Arrays.toString(variables) +
				", method=" + method.getName() +
				", clazz=" + clazz.getName() +
				'}';
	}


	private class MethodVariable {

		private final ParameterType type;

		public MethodVariable(final ParameterType type) {
			this.type = type;
		}

		public ParameterType getType() {
			return type;
		}

		@Override
		public String toString() {
			return "MethodVariable{" +
					"type=" + type +
					'}';
		}
	}
}
