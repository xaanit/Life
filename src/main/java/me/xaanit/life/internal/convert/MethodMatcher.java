package me.xaanit.life.internal.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.xaanit.life.internal.entities.MethodVariable;
import me.xaanit.life.internal.entities.Type;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Keyword;
import me.xaanit.life.internal.entities.token.Token;
import me.xaanit.life.internal.entities.token.TokenType;
import me.xaanit.life.internal.entities.token.Tokeniser;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class MethodMatcher extends Tokenisable implements Matchable<UserMethod> {


	public MethodMatcher(final Tokeniser tokeniser) {
		super(tokeniser);
	}


	@Override
	public Token<UserMethod> convert(String input, Token parent) {
		if(parent != null) {
			throw new TokeniserException("Methods should never have parents!");
		}
		final String originalInput = input + "";
		Token<UserMethod> token;
		if(!input.matches(Regex.METHOD_DECLARATION.getRegex())) {
			throw new TokeniserException("Invalid method declaration");
		}
		String methodDeclaration = input.substring(0, input.indexOf('{'));
		Pattern pattern = Regex.METHOD_INFORMATION.compile();
		Matcher m = pattern.matcher(methodDeclaration);
		if(!m.find()) {
			throw new TokeniserException("Something went horribly wrong");
			// There is something terribly wrong if this happens.
		}
		String returnType = trim(m.group(1));
		if(!Type.isValidType(returnType, true)) {
			throw new TokeniserException(
					"You can only return void, String, or a primtive that's not byte or short!");
		}
		String name = trim(m.group(2));
		if(Keyword.isKeyword(name)) {
			throw new TokeniserException("You can not use a keyword as a method name!");
		}
		String arguments = trim(m.group(3));
		MethodVariable[] variables = null;
		if(arguments.isEmpty() || arguments.equals(" ")) {
			variables = new MethodVariable[0];
		}

		if(variables == null) {
			String[] split = arguments.split(",");
			for(int i = 0; i < split.length; i++) {
				split[i] = trim(split[i]);
			}
			List<MethodVariable> vars = new ArrayList<>();
			for(String arg : split) {
				String[] args = arg.split("\\s+"); // int max for example
				if(args.length != 2) {
					throw new TokeniserException(
							"Invalid parameters, expected arguments 2, actual arguments %s, args: %s",
							args.length + "", Arrays.toString(args));
					// Means they did like String one int two instead of String one, int two
				}
				args[0] = trim(args[0]);
				args[1] = trim(args[1]);
				if(!Type.isValidType(args[0])) {
					throw new TokeniserException(
							"Only primitives (excluding byte/short) and String are allowed for parameter types! Yours: "
									+ args[0]);
					// Void is not allowed here
				}
				if(Keyword.isKeyword(args[1])) {
					throw new TokeniserException("You can not use a keyword as an argument name!");
				}
				MethodVariable variable = new MethodVariable(Type.valueOf(args[0].toUpperCase()), args[1]);
				vars.add(variable);
			}
			variables = vars.toArray(new MethodVariable[vars.size()]);
		}
		String body = input.substring(input.indexOf('{') + 1, input.lastIndexOf('}'));
		if(returnType.equals("void") && name.equals("main")) {
			token = new Token<>(originalInput, TokenType.METHOD, new UserMethod(body));
		} else {
			token = new Token<>(originalInput, TokenType.METHOD,
					new UserMethod(variables, Type.valueOf(returnType.toUpperCase()), body, name));
		}
		return token;
	}


}
