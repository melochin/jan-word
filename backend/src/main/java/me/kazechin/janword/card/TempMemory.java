package me.kazechin.janword.card;

public class TempMemory {

	private int right;

	private boolean isWrong;

	public TempMemory(){
		this(0, false);
	}

	public TempMemory(int right, boolean isWrong) {
		this.right = right;
		this.isWrong = isWrong;
	}

	public int getRight() {
		return right;
	}

	public boolean isWrong() {
		return isWrong;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public void setWrong(boolean wrong) {
		isWrong = wrong;
	}
}
