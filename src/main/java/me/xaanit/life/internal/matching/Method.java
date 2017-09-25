package me.xaanit.life.internal.matching;

import me.xaanit.life.internal.entities.Type;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Token;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@BuildParseTree
public class Method extends BaseParser<Object> implements Matcher<UserMethod> {

	@Override
	public Token<UserMethod> matchToken() {
		return null;
	}

	public Rule name() {
		return Sequence(
				returnType(),
				OneOrMore(AnyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")),
				ZeroOrMore(AnyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789"))
		);
	}

	public Rule returnType() {
		return Sequence(
				def(),
				OneOrMore(Type.values())
		);
	}

	public Rule def() {
		return OneOrMore("def");
	}
}
