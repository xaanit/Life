package me.xaanit.life.internal.entities.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import me.xaanit.life.internal.convert.MethodMatcher;
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

	public Stack<Token> tokenise(String toTokenise) {
		Stack<Token> tokens = new Stack<>();
		return tokens;
	}


	private String findBrackets(String input) {
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
			throw new TokeniserException("Unbalanced brackets");
		}
		return input.substring(0, indexOfLast + 1);
	}
}
