package me.kazechin.janword.model;

/**
 * 记忆对象个数的限制
 * + 新对象的数目
 * + 老对象的数目
 * + 易错对象的数目
 *
 * 该类不仅可以限制每次记忆任务的对象数量，而且控制每种类别对象的数量。
 *
 */
public class MemoryLimit {

	private int newLimit;

	private int oldLimit;

	private int wrongLimit;

	public MemoryLimit(int newLimit, int oldLimit, int wrongLimit) {
		this.newLimit = newLimit;
		this.oldLimit = oldLimit;
		this.wrongLimit = wrongLimit;
	}

	public int getNewLimit() {
		return newLimit;
	}

	public int getOldLimit() {
		return oldLimit;
	}

	public int getWrongLimit() {
		return wrongLimit;
	}
}
