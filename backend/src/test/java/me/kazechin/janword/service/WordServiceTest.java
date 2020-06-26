package me.kazechin.janword.service;

import me.kazechin.janword.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordServiceTest {

	private WordService wordService;

	@BeforeEach
	public void setup() {
		wordService = new WordService();
	}


	@Test
	void convertToWordsNormalCase() {

		String singleLine = "word chinese ";

		List<Word> words = wordService.convertToWords(singleLine);
		assertEquals(1, words.size());

		assertEquals("word", words.get(0).getWord());
		assertEquals(null, words.get(0).getGana());
		assertEquals("chinese", words.get(0).getChinese());


		String multiLine = "word chinese \n" +
				"はい　ハイ　hi";

		words = wordService.convertToWords(multiLine);
		assertEquals(2, words.size());
		assertEquals("はい", words.get(1).getWord());
		assertEquals("ハイ", words.get(1).getGana());
		assertEquals("hi", words.get(1).getChinese());


		String multiLineWithEmpty = "word chinese \n" +
				" \n";
		words = wordService.convertToWords(multiLineWithEmpty);
		assertEquals(1, words.size());

	}

	@Test
	void convertToWordsWrongFormCase() {

		String empty = "";

		try {
			wordService.convertToWords(empty);
		} catch (ValidationException exception) {
			fail("空字符串抛出异常");
		}

		String fouProps = "test test test test";

		try {
			wordService.convertToWords(fouProps);
			fail("过多属性没有抛出异常");
		} catch (ValidationException exception) {
			System.out.println(exception.getMessage());
		}


	}

	@Test
	public void convertToWordTestLines() {
		String lines = "おはようございます 早上好\n" +
				"\n" +
				"いただきます 开动了\n" +
				"\n" +
				"ごちそうさまでした 谢谢招待\n" +
				"\n" +
				"よろしくお願いします 请多观照\n" +
				"\n" +
				"いってきます\t我出门了\n" +
				"\n" +
				"お先に失礼します\t我先走了";
		List<Word> words = wordService.convertToWords(lines);
		assertEquals(6, words.size());
	}

}