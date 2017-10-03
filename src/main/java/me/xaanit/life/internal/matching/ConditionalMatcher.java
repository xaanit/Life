package me.xaanit.life.internal.matching;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class ConditionalMatcher {

	private static AtomicInteger integer = new AtomicInteger();

	public static boolean match(String conditional) {
		System.out.println("CONDITIONAL: " + conditional);
		if(integer.get() == 10) {
			System.exit(0);
		}
		integer.incrementAndGet();
		if(!validate(conditional)) {
			throw new TokeniserException(conditional + " is not a valid conditional!");
		}
		if(!conditional.contains("(") && !conditional.contains(")")) {
			return handle(conditional, false);
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
			conditional = conditional
					.replace(temp, handle(temp, conditional.charAt(lastIndexOfOpen - 1) == '!') + "");
			System.out.println("CONDITIONAL: " + conditional);
			System.out.println();
			return match(conditional);
		}
	}

	private static boolean handle(String part, boolean isFalse) {
		part = part.replaceAll("\\s+", "");
		if(part.equals("true") || part.equals("false")) {
			return part.equals("true");
		} else {
			if(part.contains("&&")) {
				String[] split = part.split("&&");
				if(split.length == 1) {
					throw new TokeniserException("Invalid conditional in if statement!");
				}
				String find = "";
				String before = "";
				String after = "";
				System.out.println("SPLIT: " + Arrays.toString(split));
				for(int i = 0; i < split.length; i++) {
					if(!split[i].equals("true") && !split[i].equals("false")) {
						find = split[i];
						before = Tokenisable.trim(combine(split, 0, i)).replaceAll("&&", "")
								.replaceAll("\\s+", " && ");
						after = Tokenisable.trim(combine(split, i + 1, -1)).replaceAll("\\s+", " && ");
						break;
					}
				}
				if(after.isEmpty() && !before.isEmpty()) {
					before += "&&";
				}
				System.out.println("find: " + find);
				System.out.println("before: " + before);
				System.out.println("after: " + after);
				System.out.printf("Full: %smatch(%s)%s\n",
						before.isEmpty() ? "" : before + (!after.isEmpty() ? " && " : ""), find,
						after.isEmpty() ? "" : " && " + after);

				return true;
			}
			String temp = "";
			boolean bool = false;
			boolean not = false;
			return handle(part.replace(temp, Boolean.toString(bool)), not);
		}
	}

	private static String combine(String[] args, int start, int end) {
		end = end == -1 ? args.length : end;
		start = start == -1 ? 0 : start;
		StringBuilder res = new StringBuilder();
		for(int i = start; i < end; i++) {
			res.append(args[i]).append(i == end - 1 ? "" : " ");
		}
		return res.toString();
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
