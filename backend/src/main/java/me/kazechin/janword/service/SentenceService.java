package me.kazechin.janword.service;

import me.kazechin.janword.dao.SentenceDao;
import me.kazechin.janword.model.Grammar;
import me.kazechin.janword.model.Sentence;
import me.kazechin.janword.model.Type;
import me.kazechin.janword.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentenceService {

	@Autowired
	private SentenceDao sentenceDao;

	/**
	 * 新增例句
	 * @param sentences
	 * @param id
	 * @param type
	 */
	public void add(List<Sentence> sentences, Integer id, Type type) {
		if (sentences != null) {
			for(Sentence sentence : sentences) {
				sentenceDao.add(sentence.getSentence(), id, type.getValue());
			}
		}
	}

	/**
	 * 更新例句
	 * @param sentences
	 * @param id
	 * @param type
	 */
	public void update(List<Sentence> sentences, Integer id, Type type) {
		sentenceDao.remove(id, type.getValue());
		for(Sentence sentence : sentences) {
			sentenceDao.add(sentence.getSentence(), id, type.getValue());
		}
	}

	/**
	 * 删除例句
	 * @param id
	 * @param type
	 */
	public void remove(Integer id, Type type) {
		sentenceDao.remove(id, type.getValue());
	}

	/**
	 * 包装对象：为对象的sentences字段赋值
	 * @see me.kazechin.janword.model.Word
	 * @see me.kazechin.janword.model.Grammar
	 *
	 * @param objects
	 * @param <T>
	 * @return
	 */
	public <T> List<T> wrap(List<T> objects) {
		if (objects == null) return objects;

		for(Object object : objects) {
			if (object instanceof Word) {
				Word word = (Word)object;
				word.setSentences(sentenceDao.list(word.getId(), Type.WORD.getValue()));
			} else if (object instanceof Grammar) {
				Grammar grammar = (Grammar)object;
				grammar.setSentences(sentenceDao.list(grammar.getId(), Type.GRAMMAR.getValue()));
			}
		}

		return objects;
	}

}
