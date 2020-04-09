package me.kazechin.janword.extra.kuromoji;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class KuromojiService {

	private Tokenizer tokenizer;

	public KuromojiService() {
		this.tokenizer = new Tokenizer();
	}

	public String getReading(String word, boolean needHira) {
		List<Token> tokens = tokenizer.tokenize(word);

		String reading = tokens.stream()
				.map(token -> token.getReading())
				.reduce("", (left, right) -> left+right);

		if (needHira == false) return reading;

		return transformKataToHira(reading);
	}

	/**
	 *
	 * @param word
	 * @return
	 */
	public List<String[]> getReadings(String word) {
		List<Token> tokens = tokenizer.tokenize(word);
		return tokens.stream()
				.map(t -> getSurfaceAndReadingArrary(t))
				.collect(toList());
	}

	/**
	 * 获得[surface, reading]的数组
	 * surface和reading相同的情况，reading为空字符串
	 * reading采用平假名
	 * @param t
	 * @return
	 */
	private String[] getSurfaceAndReadingArrary(Token t) {
		String hira = transformKataToHira(t.getReading());
		if (t.getSurface().equals(t.getReading()) || t.getSurface().equals(hira)) {
			return new String[]{t.getSurface(), ""};
		}
		return new String[]{t.getSurface(), hira};
	}

	/**
	 * 片假名 -> 平假名
	 * @param word
	 * @return
	 */
	public String transformKataToHira(String word) {
		StringBuilder res = new StringBuilder();
		for(Character c : word.toCharArray()) {
			Character hira = KatakanaMap.get(c);
			if (hira == null) {
				System.out.println(c);
				res.append("");
			} else {
				res.append(hira);
			}
		}
		return res.toString();
	}

	public static void main(String[] args) {
		KuromojiService kuromojiService = new KuromojiService();

		kuromojiService.getReadings("私は毎朝パンを食べます")
				.stream()
				.forEach(strs -> System.out.println(Arrays.toString(strs)));
	}
}
