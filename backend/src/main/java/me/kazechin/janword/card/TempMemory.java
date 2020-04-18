package me.kazechin.janword.card;

/**
 * 记录 记忆中的情况
 */
public class TempMemory {
	// 正确次数
	private int right;
	// 是否出现过错误
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
