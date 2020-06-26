package me.kazechin.janword.model;

public enum Type {
	WORD(0), GRAMMAR(1);

	private int value;

	Type(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
