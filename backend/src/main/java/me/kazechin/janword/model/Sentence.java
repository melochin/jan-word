package me.kazechin.janword.model;

import java.util.List;

/**
 * 例句
 * @see me.kazechin.janword.model.Grammar 的例句
 */
// TODO 考虑单词的例句
public class Sentence {

	private Integer id;

	private String sentence;

	// DB：不存该字段，需要读音时进行填充
	private List<String[]> readings;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List<String[]> getReadings() {
		return readings;
	}

	public void setReadings(List<String[]> readings) {
		this.readings = readings;
	}
}
