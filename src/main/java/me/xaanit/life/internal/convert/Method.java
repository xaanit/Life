package me.xaanit.life.internal.convert;

import java.util.regex.Pattern;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Token;

public class Method implements Matcher<UserMethod> {


	@Override
	public Token<UserMethod> convert() {
		Token<UserMethod> token = null;
		Pattern p = MatcherVariables.METHOD.getPattern();
		return token;
	}




}
