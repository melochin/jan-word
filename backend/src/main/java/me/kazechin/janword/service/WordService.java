package me.kazechin.janword.service;

import me.kazechin.janword.model.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class WordService {

	private Logger logger = LoggerFactory.getLogger(WordService.class);

	public List<Word> convertToWords(@NonNull String text) {

		List<String> lines = Arrays.asList(text.split("\\n"));
		List<String> filerLines = lines.stream()
				.map(String::trim)
				.filter(line -> !line.isEmpty())
				.collect(toList());
		try {
			return filerLines.stream()
					.map(this::convertToWord)
					.collect(toList());
		} catch (ValidationException e) {
			logger.debug("单词批处理转换出错，数据为{}", filerLines);
			throw e;
		}
	}

	private Word convertToWord(String line) throws ValidationException {
		Word word = new Word();

		// 输入法切换可能会用到日语的空格
		String[] props = line.split("[\\s|　]+");

		if (props == null) {
			throw new ValidationException("word text [" + line + "] with length ");
		}

		switch (props.length) {
			case 2:
				word.setWord(props[0]);
				word.setChinese(props[1]);
				break;
			case 3:
				word.setWord(props[0]);
				word.setGana(props[1]);
				word.setChinese(props[2]);
				break;
			default:
				throw new ValidationException("word text [" + line + "] with length " + props.length);
		}

		return word;
	}

}
