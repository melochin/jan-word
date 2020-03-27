package me.kazechin.janword.word;

public class Word {

	private int id;

	private String word;

	private String gana;

	private String chinese;

	private String origin;

	public Word() {
	}

	public Word(String word, String gana, String chinese) {
		this.word = word;
		this.gana = gana;
		this.chinese = chinese;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Word)) return false;

		Word word1 = (Word) o;

		if (id != word1.id) return false;
		if (word != null ? !word.equals(word1.word) : word1.word != null) return false;
		if (gana != null ? !gana.equals(word1.gana) : word1.gana != null) return false;
		if (chinese != null ? !chinese.equals(word1.chinese) : word1.chinese != null) return false;
		return origin != null ? origin.equals(word1.origin) : word1.origin == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (word != null ? word.hashCode() : 0);
		result = 31 * result + (gana != null ? gana.hashCode() : 0);
		result = 31 * result + (chinese != null ? chinese.hashCode() : 0);
		result = 31 * result + (origin != null ? origin.hashCode() : 0);
		return result;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

}
