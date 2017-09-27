package me.xaanit.life.internal.convert;

import me.xaanit.life.internal.entities.Type;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Token;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@BuildParseTree
public class Method extends BaseParser<Object> implements Matcher<UserMethod> {

	@Override
	public Token<UserMethod> convert() {
		Token<UserMethod> token = null;

		return token;
	}

	public Rule parameters() {
		return Sequence(
				name(),
				paren(true),
				space(true),
				ZeroOrMore(paramType()),
				space(true),
				paren(false),
				EOI
		);
	}

	public Rule paramType() {
		return Sequence(
				Optional(String("const")),
				FirstOf(Type.STRING.getName(), Type.CHAR.getName(), Type.LONG.getName(),
						Type.FLOAT.getName(), Type.BOOLEAN.getName(),
						Type.DOUBLE.getName(), Type.INT
								.getName()),
				space(),
				identifer(),
				Optional(String(',')),
				space(true)
		);
	}

	public Rule paren(boolean open) {
		return open ? String('(') : String(')');
	}

	public Rule name() {
		return Sequence(
				returnType(),
				identifer(),
				space(true)
		);
	}

	public Rule returnType() {
		return Sequence(
				def(),
				FirstOf(Type.STRING.getName(), Type.CHAR.getName(), Type.VOID.getName(),
						Type.LONG.getName(), Type.FLOAT.getName(), Type.BOOLEAN.getName(),
						Type.DOUBLE.getName(), Type.INT
								.getName()),
				space()
		);
	}

	public Rule def() {
		return Sequence(String("def"), space());
	}


	public Rule space() {
		return space(false);
	}

	public Rule space(boolean zero) {
		return zero ? ZeroOrMore(" ") : OneOrMore(" ");
	}

	public Rule identifer() {
		return Sequence(
				OneOrMore(AnyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")),
				ZeroOrMore(
						AnyOf(
								"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789".toCharArray()))
		);
	}


}
