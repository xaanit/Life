package me.xaanit.life.internal.entities;

public enum Type {
	STRING("String"),
	DOUBLE,
	CHAR,
	FLOAT,
	LONG,
	INT,
	BOOLEAN,
	VOID;


	private final String name;

	Type(final String name) {
		this.name = name;
	}

	Type() {
		this.name = toString().toLowerCase();
	}

	public String getName() {
		return this.name;
	}

	public static boolean isValidType(String str, boolean includeVoid) {
		for(Type type : values()) {
			if(str.equals(type.name)) { // Exact. int is okay, INT is not
				if(!includeVoid && str.equals("void")) { // Can safely use equals, if it's not lowercase the above if won't trigger
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isValidType(String str) {
		return isValidType(str, false);
	}
}
