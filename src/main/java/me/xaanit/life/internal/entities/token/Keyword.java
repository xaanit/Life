package me.xaanit.life.internal.entities.token;

public enum Keyword {
	CHAR,
	LONG,
	INT,
	STRING("String"),
	BOOLEAN,
	FLOAT,
	DOUBLE,
	CONST,
	VOID,
	DEF,
	ELIF,
	ELSE,
	IF,
	FOR,
	WHILE;


	private String identifcation;

	Keyword(String identifcation) {
		this.identifcation = identifcation;
	}

	Keyword() {
		this.identifcation = toString().toLowerCase();
	}

	public static boolean isKeyword(String token) {
		for(Keyword keyword : values()) {
			if(keyword.identifcation.equals(token)) {
				return true;
			}
		}
		return false;
	}
}
