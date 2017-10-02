package me.xaanit.life.internal.entities;

public class Statement {

	private final String statement;

	public Statement(String statement) {
		this.statement = statement;
	}

	public void handle() {}

	public String getStatement() {
		return statement;
	}
}
