package me.xaanit.life.internal.entities;

public class IfStatement {

	private final String body;
	private final String condition;
	private final boolean isElseIf;
	private final boolean isElse;

	private IfStatement(String body, String condition, boolean isElseIf, boolean isElse) {
		this.body = body;
		this.condition = condition;
		this.isElseIf = isElseIf;
		this.isElse = isElse;
	}

	public IfStatement(String body, String condition) {
		this(body, condition, false, false);
	}

	public IfStatement(String body) {
		this(body, "", false, true);
	}

	public IfStatement(String body, String condition, boolean isElseIf) {
		this(body, condition, isElseIf, false);
	}


	public String getBody() {
		return body;
	}

	public String getCondition() {
		return condition;
	}

	public boolean isElseIf() {
		return isElseIf;
	}

	public boolean isElse() {
		return isElse;
	}
}
