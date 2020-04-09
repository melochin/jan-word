package me.kazechin.janword.extra.kuromoji;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class KuromojiController {

	private KuromojiService service;

	@Autowired
	public KuromojiController(KuromojiService service) {
		this.service = service;
	}

	/**
	 * 返回每个单词的读音
	 * 比如：パンを食べます
	 * 返回：
	 * [パン, ],[を, ],[食べ, たべ],[ます, ]
	 *
	 * @param word
	 * @return
	 */
	@GetMapping("/kuromoji/reading/{word}")
	public List<String[]> reading(@PathVariable("word") String word) {
		return service.getReadings(word);
	}

}
