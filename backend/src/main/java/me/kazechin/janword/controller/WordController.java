package me.kazechin.janword.controller;

import com.github.pagehelper.PageHelper;
import me.kazechin.janword.dao.SentenceDao;
import me.kazechin.janword.dao.WordDao;
import me.kazechin.janword.model.PageResult;
import me.kazechin.janword.model.Type;
import me.kazechin.janword.model.Word;
import me.kazechin.janword.service.SentenceService;
import me.kazechin.janword.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Word CRUD API
 */
@CrossOrigin
@RestController
public class WordController {

	@Autowired
	private WordDao wordDao;

	@Autowired
	private SentenceService sentenceService;

	@Autowired
	private WordService wordService;

	@GetMapping("/words")
	public PageResult<Word> list(@RequestParam(name = "page", required = false, defaultValue = "1")
									   Integer page) {

		PageHelper.startPage(page, 10);


		List<Word> words = wordDao.list();

		words = sentenceService.wrap(words);

		return new PageResult<>(words);
	}

	/**
	 * 校验是否存在重复的假名
	 * @param gana
	 * @return
	 */
	@GetMapping("/words/{gana}")
	public PageResult<Word> listGana(@PathVariable("gana") String gana) {
		return new PageResult<>(wordDao.listByGana(gana));
	}

	@Transactional
	@PostMapping("/word")
	public void add(@RequestBody Word word) {
		wordDao.add(word);
		sentenceService.add(word.getSentences(), word.getId(), Type.WORD);
	}

	/**
	 * 以CSV格式的文本方式，批量导入单词
	 * 分隔符采用tab或者空格
	 *
	 * 成功 - 200
	 * 错误 - 500
	 * @param text
	 */
	@Transactional
	@PostMapping("/word/batch")
	public void addBatch(@RequestBody String text) {
		List<Word> words = wordService.convertToWords(text);
		for(Word word : words) {
			wordDao.add(word);
		}
	}

	@Transactional
	@PutMapping("/word")
	public void modify(@RequestBody Word word) {
		wordDao.modify(word);
		sentenceService.update(word.getSentences(), word.getId(), Type.WORD);
	}

	@Transactional
	@DeleteMapping("/word/{id}")
	public void remove(@PathVariable int id) {
		wordDao.remove(id);
		sentenceService.remove(id, Type.WORD);
	}

}
