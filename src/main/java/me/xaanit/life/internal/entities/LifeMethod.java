package me.xaanit.life.internal.entities;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import me.xaanit.life.internal.entities.enums.ParameterType;
import me.xaanit.life.internal.exceptions.LifeException;

public class LifeMethod {

	public final MethodVariable[] variables;
	public final Method method;
	public final Class clazz;


	public LifeMethod(final Class clazz, final Method method) {
		this.clazz = clazz; // We pass in the class just to be sure
		this.method = method;
		final Parameter[] parameters = method.getParameters(); // To loop through
		this.variables = new MethodVariable[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			ParameterType type;
			String info = parameters[i].getClass()
					.getName(); // all primitives are just the name, string is java.lang.String
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

		/*String returnType = method.getReturnType().getName();
		if(!(returnType.equalsIgnoreCase("long") || returnType.equalsIgnoreCase("int") || returnType
				.equalsIgnoreCase("double") || returnType.equalsIgnoreCase("boolean") || returnType)) {
			throw new LifeException(
					"Methods can only return primitives (excluding byte and short) or Strings!");
		}*/

	}

	private boolean equalsAny(Object o, Object... objects) {
		for(Object object : objects) {
			if(o.equals(object)) {
				return true;
			}
		}
		return false;
	}


	private class MethodVariable {

		private final ParameterType type;

		public MethodVariable(final ParameterType type) {
			this.type = type;
		}
	}
}
