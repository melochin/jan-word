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

	public List<String[]> getReadings(String word, boolean needHira) {
		List<Token> tokens = tokenizer.tokenize(word);
		List<String[]> res= tokens.stream()
				.map(t -> new String[]{t.getSurface(), t.getReading()})
				.collect(toList());

		if (needHira == false) return res;

		for(String[] strs : res) {
			strs[1] = transformKataToHira(strs[1]);
		}

		return res;
	}

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

		kuromojiService.getReadings("間", true)
				.stream()
				.forEach(strs -> System.out.println(Arrays.toString(strs)));
	}
}
