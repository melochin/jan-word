package me.kazechin.janword.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class WordController {

	@Autowired
	private WordDao wordDao;

	@GetMapping("/words")
	public List<Word> list() {
		return wordDao.list();
	}

	@PostMapping("/word")
	public void add(@RequestBody Word word) {
		wordDao.add(word);
	}

	@PutMapping("/word")
	public void modify(@RequestBody Word word) {
		wordDao.modify(word);
	}

	@DeleteMapping("/word/{id}")
	public void remove(@PathVariable int id) {
		wordDao.remove(id);
	}

}
