package me.kazechin.janword.word;

public class Word {

	private int id;

	private String word;

	private String gana;

	private String chinese;

	public Word() {
	}

	public Word(String word, String gana, String chinese) {
		this.word = word;
		this.gana = gana;
		this.chinese = chinese;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getGana() {
		return gana;
	}

	public void setGana(String gana) {
		this.gana = gana;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}
}
