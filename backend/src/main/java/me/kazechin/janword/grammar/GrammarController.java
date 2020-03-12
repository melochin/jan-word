package me.kazechin.janword.grammar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class GrammarController {

	@Autowired
	private GrammarDao grammarDao;

	@Autowired
	private SentenceDao sentenceDao;

	@Transactional
	@PostMapping("/grammar")
	public void add(@RequestBody Grammar grammar) {
		// get grammar id
		grammarDao.add(grammar);
		assert grammar.getId() > 0;
		if (grammar.getSentences() != null) {
			grammar.getSentences()
					.forEach(s -> sentenceDao.add(s.getSentence(), grammar.getId()));
		}
	}

	@GetMapping("/grammars")
	public List<Grammar> list() {
		List<Grammar> grammars = grammarDao.list();
		grammars.forEach(g -> g.setSentences(sentenceDao.list(g.getId())));
		return grammars;
	}

	@DeleteMapping("/grammar/{id}")
	public void remove(@PathVariable int id) {
		grammarDao.remove(id);
	}

	@PutMapping("/grammar")
	public void modify(@RequestBody Grammar grammar) {
		grammarDao.update(grammar);
		List<Sentence> sentences = grammar.getSentences();
		sentenceDao.remove(grammar.getId());
		sentences.forEach(s -> sentenceDao.add(
				s.getSentence(), grammar.getId())
		);
	}

}
