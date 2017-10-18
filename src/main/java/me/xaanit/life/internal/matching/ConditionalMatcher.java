package me.xaanit.life.internal.matching;

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
		part = Tokenisable.trim(part);
		if(part.equals("true") || part.equals("false")) {
			return isFalse ? part.equals("false") : part.equals("true");
		} else {
			if(part.contains("&&")) {
				return handleAnd(part);
			}
			if(part.contains("||")) {
				return handleOr(part);
			}
			String temp = "";
			boolean bool = false;
			boolean not = false;
			return handle(part.replace(temp, Boolean.toString(bool)), not);
		}
	}

	public static boolean handleOr(String str) {
		System.out.println("OR: " + str);
		if(str.equals("true") || str.equals("false")) {
			return str.equals("true");
		}
		String[] arr = str.split("\\|\\|");
		if(arr.length == 2) {
			if(!arr[0].equals("true") && !arr[0].equals("false")) {
				System.out.println("OR HAD TO GET ARR[0] " + arr[0]);
				arr[0] = handle(arr[0], false) + "";
				System.out.println("ARR[0] AFTER (OR) " + arr[0]);
			}
			if(!arr[1].equals("true") && !arr[1].equals("false")) {
				System.out.println("OR HAD TO GET ARR[1]");
				arr[1] = handle(arr[1], false) + "";
				System.out.println("ARR[1] AFTER (OR) " + arr[0]);
			}

			return arr[0].equals("true") || arr[1].equals("true");

		} else {
			String first = handleOr(combine(arr, 0, 2, "||")) + "";
			String second = combine(arr, 2, arr.length, "||");
			return handleOr(first + (!second.isEmpty() ? "||" : "") + second);
		}
	}

	public static boolean handleAnd(String str) {
		System.out.println("AND: " + str);
		if(str.equals("true") || str.equals("false")) {
			return str.equals("true");
		}
		String[] arr = str.split("&&");
		if(arr.length == 2) {
			if(!arr[0].equals("true") && !arr[0].equals("false")) {
				arr[0] = handle(arr[0], false) + "";
			}
			if(!arr[1].equals("true") && !arr[1].equals("false")) {
				arr[1] = handle(arr[1], false) + "";
			}

			return arr[0].equals("true") && arr[1].equals("true");

		} else {
			String first = handleAnd(combine(arr, 0, 2, "&&")) + "";
			String second = combine(arr, 2, arr.length, "&&");
			return handleAnd(first + (!second.isEmpty() ? "&&" : "") + second);
		}
	}


	private static boolean handleNumbers(String str) {
		boolean bool;
		String[] arr = str.split("\\s+");
		if(arr.length != 3) {
			throw new TokeniserException("Invalid conditional: " + str);
		}
		try {
			check(arr[0]);
			check(arr[2]);
		} catch(NumberFormatException ex) {
			throw new TokeniserException("Invalid conditional: " + str);
		}
		switch(arr[1]) {
			case "<":
				bool = lessThan(convert(arr[0]), convert(arr[2]));
				break;
			case ">":
				bool = !lessThan(convert(arr[0]), convert(arr[2]));
				break;
			case "==":
				bool = equal(convert(arr[0]), convert(arr[2]));
				break;
			case "!=":
				bool = !equal(convert(arr[0]), convert(arr[2]));
				break;
			case "<=":
				bool =
						equal(convert(arr[0]), convert(arr[2])) || lessThan(convert(arr[0]), convert(arr[2]));
				break;
			case ">=":
				bool =
						equal(convert(arr[0]), convert(arr[2])) || !lessThan(convert(arr[0]), convert(arr[2]));
				break;
			default:
				throw new TokeniserException("Invalid conditional: " + str);
		}
		return bool;
	}


	private static Object convert(String str) {
		if(str.matches(Regex.FLOAT.getRegex())) {
			return Float.parseFloat(str.replaceAll("[fF]", ""));
		} else if(str.matches(Regex.DOUBLE.getRegex())) {
			return Double.parseDouble(str.replaceAll("[dD]", ""));
		} else if(str.matches(Regex.LONG.getRegex())) {
			return Long.parseLong(str.replaceAll("[lL]", ""));
		} else {
			return Integer.parseInt(str);
		}
	}

	private static boolean lessThan(Object one, Object two) {
		//
		if(one instanceof Integer) {
			int _1 = (int) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 < _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 < _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 < _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 < _2;
			}
		}

		//
		if(one instanceof Double) {
			double _1 = (double) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 < _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 < _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 < _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 < _2;
			}
		}

		//
		if(one instanceof Float) {
			float _1 = (float) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 < _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 < _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 < _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 < _2;
			}
		}

		//
		if(one instanceof Long) {
			long _1 = (long) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 < _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 < _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 < _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 < _2;
			}
		}

		return false; // Shouldn't ever reach. But yay failsafes.
	}

	private static boolean equal(Object one, Object two) {
		//
		if(one instanceof Integer) {
			int _1 = (int) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 == _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 == _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 == _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 == _2;
			}
		}

		//
		if(one instanceof Double) {
			double _1 = (double) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 == _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 == _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 == _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 == _2;
			}
		}

		//
		if(one instanceof Float) {
			float _1 = (float) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 == _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 == _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 == _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 == _2;
			}
		}

		//
		if(one instanceof Long) {
			long _1 = (long) one;
			if(two instanceof Integer) {
				int _2 = (int) two;
				return _1 == _2;
			}

			if(two instanceof Double) {
				double _2 = (double) two;
				return _1 == _2;
			}

			if(two instanceof Long) {
				long _2 = (long) two;
				return _1 == _2;
			}

			if(two instanceof Float) {
				float _2 = (float) two;
				return _1 == _2;
			}
		}

		return false; // Shouldn't ever reach. But yay failsafes.
	}


	private static void check(String str) throws NumberFormatException {
		int f = 0;
		int t = 0;

		t++;
		try {
			Integer.parseInt(str);
		} catch(NumberFormatException ignored) {
			f++;
		}

		t++;
		try {
			Long.parseLong(str);
		} catch(NumberFormatException ignored) {
			f++;
		}

		t++;
		try {
			Float.parseFloat(str);
		} catch(NumberFormatException ignored) {
			f++;
		}

		t++;
		try {
			Double.parseDouble(str);
		} catch(NumberFormatException ex) {
			f++;
		}

		if(t == f) { // Total is failed. Meaning not a number.
			throw new NumberFormatException();
		}
	}

	private static String combine(String[] args, int start, int end) {
		return combine(args, start, end, " ");
	}

	private static String combine(String[] args, int start, int end, CharSequence operator) {
		end = end == -1 ? args.length : end;
		start = start == -1 ? 0 : start;
		StringBuilder res = new StringBuilder();
		for(int i = start; i < end; i++) {
			res.append(Tokenisable.trim(args[i])).append(i == end - 1 ? "" : operator);
		}
		String ret = res.toString();
		return ret;
	}


	private static String[] trim(String[] arr) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = Tokenisable.trim(arr[i]);
		}
		return arr;
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
