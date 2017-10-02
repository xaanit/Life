package me.xaanit.life.internal.entities;

public class ForLoop {

	private final long from;
	private final long to;
	private final String body;
	private final UserVariable variableBy;

	public ForLoop(final long from, final long to, final String body,
			final UserVariable variableBy) {
		this.from = from;
		this.to = to;
		this.body = body;
		this.variableBy = variableBy;
	}

	public long getFrom() {
		return from;
	}

	public long getTo() {
		return to;
	}

	public String getBody() {
		return body;
	}

	public UserVariable getVariableBy() {
		return variableBy;
	}
}
