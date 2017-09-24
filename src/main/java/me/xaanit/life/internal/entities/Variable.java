package me.xaanit.life.internal.entities;

/**
 * Represents a variable (usually from a method). I.e
 */
public class Variable {

	private final ParameterType type;
	private final String info;


	public Variable(ParameterType type, String info) {
		this.type = type;
		this.info = info;
	}

	public Object convert() {
		switch(type) {
			case INT:
				return Integer.parseInt(info);
			case CHAR:
				return info.charAt(1);
			case LONG:
				return Long.parseLong(info.replaceAll("[lL]", ""));
			case FLOAT:
				return Float.parseFloat(info.replaceAll("[Ff]", ""));
			case DOUBLE:
				return Double.parseDouble(info);
			case BOOLEAN:
				return info.equalsIgnoreCase("true");
			default:
				return info.substring(1, info.length() - 1);
		}
	}

	public String getInfo() {
		return this.info;
	}

	public ParameterType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "Variable{" +
				"type=" + type +
				", info='" + info + '\'' +
				'}';
	}
}
