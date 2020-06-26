package me.kazechin.janword.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 记忆中的状态：用户在记忆对象时，记录对象被记住了多少次？被记错了多少次？
 * 比如，用户记忆一个单词时，记得对3次，记错1次，连续记得3次，则
 * {memoryingStatus: {correctCount:3, wrongCount:1, continuousCorrectCount:3}}
 *
 * MemoryingStatus主要用来筛选用户记忆的对象是否需要在本次记忆任务中再次出现。
 * 比如，正确率达到一定程度，可以认为在本次记忆任务中，用户掌握了该对象，然后将对象从记忆队列中剔除。
 *
 */
public class MemoryingStatus {

	// 正确次数
	private int correctCount;
	// 错误次数
	private int wrongCount;
	// 连续正确次数
	private int continuousCorrectCount;

	public MemoryingStatus(){
		correctCount = 0;
		wrongCount = 0;
		continuousCorrectCount = 0;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	@JsonIgnore
	public boolean isWrong() {
		return wrongCount > 0;
	}

	public int getContinuousCorrectCount() {
		return continuousCorrectCount;
	}

	public void setContinuousCorrectCount(int continuousCorrectCount) {
		this.continuousCorrectCount = continuousCorrectCount;
	}
}
