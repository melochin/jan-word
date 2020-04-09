package me.kazechin.janword.grammar;

import java.util.List;

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
