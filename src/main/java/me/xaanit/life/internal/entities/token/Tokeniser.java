package me.xaanit.life.internal.entities.token;

import java.util.ArrayList;
import java.util.List;
import me.xaanit.life.internal.convert.MethodMatcher;
import me.xaanit.life.internal.convert.Regex;
import me.xaanit.life.internal.convert.Tokenisable;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.executors.LifeTask;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class Tokeniser {

	private final MethodMatcher method = new MethodMatcher(this);
	private final LifeTask task;
	protected final List<UserMethod> methods = new ArrayList<>();

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
			if(toTokenise.startsWith("def")) {
				String[] found = findBrackets(toTokenise);
				end = Integer.parseInt(found[1]);
				Token<UserMethod> token = method.convert(found[0]);
				tokens.add(token);
			}
			toTokenise = toTokenise.substring(end);
		}
		return tokens;
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
