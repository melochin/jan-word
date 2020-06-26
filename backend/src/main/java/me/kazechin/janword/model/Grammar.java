package me.kazechin.janword.model;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Grammar)) return false;

		Grammar grammar1 = (Grammar) o;

		if (id != grammar1.id) return false;
		if (grammar != null ? !grammar.equals(grammar1.grammar) : grammar1.grammar != null) return false;
		if (detail != null ? !detail.equals(grammar1.detail) : grammar1.detail != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (grammar != null ? grammar.hashCode() : 0);
		result = 31 * result + (detail != null ? detail.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GRAMMAR{" +
				"id=" + id +
				", grammar='" + grammar + '\'' +
				", detail='" + detail + '\'' +
				", sentences=" + sentences +
				'}';
	}
}
