package me.xaanit.life.internal.entities.token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.xaanit.life.internal.convert.MethodMatcher;
import me.xaanit.life.internal.convert.Regex;
import me.xaanit.life.internal.convert.Tokenisable;
import me.xaanit.life.internal.entities.Type;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.UserVariable;
import me.xaanit.life.internal.entities.executors.LifeTask;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class Tokeniser {

	private final MethodMatcher method = new MethodMatcher(this);
	private final LifeTask task;
	protected final List<UserMethod> methods = new ArrayList<>();
	protected final List<UserVariable> variables = new ArrayList<>();

	public Tokeniser(final LifeTask task) {
		this.task = task;
	}

	public List<Token> tokenise(String toTokenise) {
		List<Token> tokens = new ArrayList<>();
		toTokenise = toTokenise.replaceAll(Regex.COMMENT.getRegex(), "")
				.replaceAll(Regex.COMMENT_MULTILINE.getRegex(), "");
		while(!toTokenise.isEmpty()) {
			toTokenise = Tokenisable.trim(toTokenise);
			int end = toTokenise.length();
			toTokenise = toTokenise.substring(end);
		}
		return tokens;
	}

	public List<Token> topLevel(String toTokenise) {
		List<Token> tokens = new ArrayList<>();
		toTokenise = toTokenise.replaceAll(Regex.COMMENT.getRegex(), "")
				.replaceAll(Regex.COMMENT_MULTILINE.getRegex(), "");
		while(!toTokenise.isEmpty()) {
			toTokenise = Tokenisable.trim(toTokenise);
			int end = toTokenise.length();
			if(toTokenise.startsWith("def")) {
				String[] found = findBrackets(toTokenise);
				end = Integer.parseInt(found[1]);
				Token<UserMethod> token = method.convert(found[0]);
				UserMethod method = token.getInfo();
				if(methods.stream().filter(m -> m.equals(method)).findFirst().isPresent()) {
					throw new TokeniserException("You can not define the same method twice!");
				}
				this.methods.add(method);
				tokens.add(token);
			} else {
				String temp = toTokenise.substring(0, toTokenise.indexOf(';') + 1);
				if(temp.matches(Regex.VARIABLE_CREATION.getRegex())) {
					Token<UserVariable> variable = null;
					Pattern p = Regex.VARIABLE_CREATION.compile();
					Matcher m = p.matcher(temp);
					m.find();
					String type = m.group(2);
					String name = m.group(3);
					String value = m.group(4);
					if(!Type.isValidType(type)) {
						throw new TokeniserException(
								"Variables can only be a primitive (excluding byte/short) or a String!");
					}
					if(Keyword.isKeyword(name)) {
						throw new TokeniserException("Variable names can not be a keyword!");
					}

					if(!valid(value, Type.valueOf(type.toUpperCase()))) {
						throw new TokeniserException("Value is not valid for type!");
					}
				} else {
					throw new TokeniserException(
							"You can only declare methods and variables at the top level!");
				}
			}
			toTokenise = toTokenise.substring(end);
		}
		return tokens;
	}

	private boolean valid(String value, Type type) {
		//TODO: Validate
		return true;
	}


	private String[] findBrackets(String input) {
		int open = 0;
		int closed = 0;
		int indexOfFirst = 0;
		int indexOfLast = input.length() - 1;
		for(int i = 0; i < input.length(); i++) {
			char current = input.charAt(i);
			if(current == '{') {
				open++;
				if(indexOfFirst == 0) {
					indexOfFirst = i;
				}
			} else if(current == '}') {
				closed++;
				indexOfLast = i;
				if(open == closed) {
					break;
				}
			}
		}

		if(open != closed) {
			throw new TokeniserException("Unbalanced brackets! Found %s open brackets, %s closed.",
					open + "", closed + "");
		}
		return new String[] {input.substring(0, indexOfLast + 1), (indexOfLast + 1) + ""};
	}
}
