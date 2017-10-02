package me.xaanit.life.internal.matching;

import java.util.concurrent.atomic.AtomicInteger;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class ConditionalMatcher {

	private static AtomicInteger integer = new AtomicInteger();

	public static boolean match(String conditional) {
		System.out.println("CONDITIONAL: " + conditional);
		if(integer.get() == 5) {
			System.exit(0);
		}
		integer.incrementAndGet();
		if(!validate(conditional)) {
			throw new TokeniserException(conditional + " is not a valid conditional!");
		}
		if(!conditional.contains("(") && !conditional.contains(")")) {
			return handle(conditional);
		} else {
			int lastIndexOfOpen = -1;
			int firstIndexOfClosed = conditional.length();
			for(int i = 0; i < conditional.length(); i++) {
				if(conditional.charAt(i) == '(') {
					if(i > lastIndexOfOpen) {
						lastIndexOfOpen = i;
					}
				}
				if(conditional.charAt(i) == ')') {
					if(i < firstIndexOfClosed) {
						firstIndexOfClosed = i;
					}
				}
			}
			String temp = conditional.substring(lastIndexOfOpen, firstIndexOfClosed + 1);
			System.out.println("TEMP: " + temp);
			conditional = conditional.replace(temp, handle(temp) + "");
			System.out.println("CONDITIONAL: " + conditional);
			System.out.println();
			return match(conditional);
		}
	}

	private static boolean handle(String part) {
		boolean bool = false;

		return bool;
	}


	private static boolean validate(String conditional) {
		int open = 0;
		int closed = 0;
		for(char c : conditional.toCharArray()) {
			if(c == '(') {
				open++;
			} else if(c == ')') {
				closed++;
			}
		}
		return open == closed;
	}
}
