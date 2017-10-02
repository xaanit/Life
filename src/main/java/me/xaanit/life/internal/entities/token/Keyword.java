package me.xaanit.life.internal.entities.token;

import me.xaanit.life.internal.entities.Type;

public enum Keyword {
	CONST,
	DEF,
	ELIF,
	ELSE,
	IF,
	FOR,
	WHILE,
	TO,
	BY;


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
		return Type.isValidType(token, true);
	}
}
