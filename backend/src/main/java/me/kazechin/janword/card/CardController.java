package me.kazechin.janword.card;

import me.kazechin.janword.mapper.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class CardController {

	@Autowired
	private WordDao wordDao;

	@GetMapping("/card/words")
	public List<List<String>> word() {
		return wordDao.list()
				.stream()
				.map(word -> Arrays.asList(word.getWord(),
						word.getGana() + " " +word.getChinese()))
				.collect(toList());
	}
}
