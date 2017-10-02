package me.xaanit.life.internal.entities;

import java.util.concurrent.atomic.AtomicLong;

public class ForLoop {

	private final long from;
	private final long to;
	private final String body;
	private final UserVariable variableBy;
	private long current;
	private AtomicLong totalRepitions = new AtomicLong();

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

	public void increment() {
		this.current++;
		this.totalRepitions.incrementAndGet();
	}

	public void decrement() {
		this.current--;
		this.totalRepitions.incrementAndGet();
	}

	public long get() {
		return this.totalRepitions.get();
	}
}
