package me.kazechin.janword.grammar;

import java.util.List;

public class Grammar {

	private int id;

	private String grammar;

	private String detail;

	private List<Sentence> sentences;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrammar() {
		return grammar;
	}

	public void setGrammar(String grammar) {
		this.grammar = grammar;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	@Override
	public String toString() {
		return "Grammar{" +
				"id=" + id +
				", grammar='" + grammar + '\'' +
				", detail='" + detail + '\'' +
				", sentences=" + sentences +
				'}';
	}
}
