package me.xaanit.life.internal.convert;

import me.xaanit.life.internal.entities.Type;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Token;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.matchers.AnyMatcher;

@BuildParseTree
public class Method extends BaseParser<Object> implements Matcher<UserMethod> {

	@Override
	public Token<UserMethod> convert() {
		Token<UserMethod> token = null;

		return token;
	}

	public Rule method() {
		return Sequence(
				parameters(),
				bracket(true),
				new AnyMatcher(),
				bracket(false),
				EOI
		);
	}

	public Rule bracket(boolean open) {
		return Sequence(
				space(true),
				ZeroOrMore(newLine()),
				String(open ? '{' : '}')
		);
	}

	public Rule parameters() {
		return Sequence(
				name(),
				paren(true),
				space(true),
				ZeroOrMore(paramType()),
				space(true),
				paren(false)
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
				Optional(String(',')).suppressSubnodes(),
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
		return Sequence(String("def").suppressSubnodes(), space());
	}


	public Rule space() {
		return space(false);
	}

	public Rule space(boolean zero) {
		return zero ? ZeroOrMore(" ").suppressSubnodes() : OneOrMore(" ").suppressSubnodes();
	}

	public Rule identifer() {
		return Sequence(
				OneOrMore(AnyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")),
				ZeroOrMore(
						AnyOf(
								"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789".toCharArray()))
		).suppressSubnodes();
	}


	public Rule newLine() {
		return FirstOf("\r", "\n", "\r\n");
	}


}
