package me.kazechin.janword.controller;

import com.github.pagehelper.PageHelper;
import me.kazechin.janword.dao.GrammarDao;
import me.kazechin.janword.model.Grammar;
import me.kazechin.janword.model.PageResult;
import me.kazechin.janword.model.Type;
import me.kazechin.janword.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Grammar CRUD API
 */
@CrossOrigin
@RestController
public class GrammarController {

	@Autowired
	private GrammarDao grammarDao;

	@Autowired
	private SentenceService sentenceService;

	@GetMapping("/grammars")
	public PageResult<Grammar> list(@RequestParam(name = "keyword", required = false) String keyword,
									@RequestParam(name = "page", required = false, defaultValue = "1")
											Integer page) {
		List<Grammar> grammars;

		PageHelper.startPage(page, 10);
		if (keyword != null) {
			grammars = grammarDao.listByKeyword(keyword);
		} else {
			grammars = grammarDao.list();
		}

		grammars = sentenceService.wrap(grammars);

		PageResult<Grammar> result = new PageResult<>(grammars);

		return result;
	}

	@Transactional
	@PostMapping("/grammar")
	public void add(@RequestBody Grammar grammar) {
		// get grammar id
		grammarDao.add(grammar);
		assert grammar.getId() > 0;

		sentenceService.add(grammar.getSentences(), grammar.getId(), Type.GRAMMAR);
	}

	@PutMapping("/grammar")
	public void modify(@RequestBody Grammar grammar) {
		grammarDao.update(grammar);
		sentenceService.update(grammar.getSentences(), grammar.getId(), Type.GRAMMAR);
	}

	@DeleteMapping("/grammar/{id}")
	public void remove(@PathVariable int id) {
		grammarDao.remove(id);
		sentenceService.remove(id, Type.GRAMMAR);
	}

}
