package me.kazechin.handleword;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kazechin.janword.model.Word;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class TransferWord {

	private final String filePath;

	private final String origin;

	public TransferWord(String filePath, String origin) {
		this.filePath = filePath;
		this.origin = origin;
	}

	private static List<String> split(String str) {
		return Arrays.stream(
				str.replaceAll("[\\s\\(\\)〔〕（）　]", " ")
						.split("\\s")
				)
				.map(word -> word.trim())
				.filter(word -> word.isEmpty() == false)
				.collect(toList());

	}

	public void parseToFile(String destPath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		List<String> lines = getLines(reader);

		List<Word> wordList = lines.stream()
				.map(TransferWord::split)
				.map(words -> {

					Word word = new Word();
					if (words.size() == 4) {
						word.setGana(words.get(0));
						word.setWord(words.get(1));
						word.setChinese(words.get(3));
					} else if (words.size() == 3) {
						word.setGana(words.get(0));
						word.setWord(words.get(0));
						word.setChinese(words.get(2));
					}
					word.setOrigin(origin);

					return word;
				})
				.collect(toList());

		new ObjectMapper().writeValue(new File(destPath), wordList);
	}

	private List<String> getLines(BufferedReader reader) {
		List<String> list = new ArrayList<>();
		while (true) {
			try {
				String str = reader.readLine().trim();
				if (str.isEmpty()) continue;
				list.add(str);
			} catch (Exception e) {
				break;
			}
		}

		return list;
	}


	public static void main(String[] args) throws IOException {
		TransferWord transferWord = new TransferWord("../词汇集/4.md", "标日第四课");
		transferWord.parseToFile("../词汇集/4.json");

//				.map(array -> {
//					Map<String, String> map = new HashMap<>();
//
//					String first = array[0];
//
//					if (first.indexOf("（") >= 0) {
//						map.put("gana", first.substring(0, first.indexOf("（")).replaceAll("　", ""));
//						map.put("word", first.substring(first.indexOf("（") + 1, first.indexOf("）")).replaceAll("　", ""));
//					} else {
//						map.put("gana", null);
//						map.put("word", first.replaceAll("　", ""));
//					}
//
//					map.put("chinese", array[array.length - 1]);
//					return map;
//				}).collect(toList());
//
//		new ObjectMapper().writeValue(new File("../词汇集/2.json"), mapList);
	}
}
