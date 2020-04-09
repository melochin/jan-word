package me.kazechin.janword.card;

import me.kazechin.janword.word.Word;
import me.kazechin.janword.word.WordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemoryWordService {

	@Autowired
	private WordDao wordDao;

	private final Logger logger = LoggerFactory.getLogger(MemoryWordService.class);

	public List<Word> list(int userId, int limit) {

		int newLimit = limit;
		int oldLimit = limit / 2;
		int wrongLimit = limit / 2;
		int total = newLimit + oldLimit + wrongLimit;

		Set<Word> uniqueWord = new HashSet<>();

		// 尚未记忆的单词
		List<Word> words = wordDao.rememberNew(userId, newLimit);
		uniqueWord.addAll(words);

		// 7天内没有记忆的单词
		// TODO 如果是记忆次数大于等于3次，且错误率小于15%的7天没有记忆的单词怎么办？
		// 记忆完成之后怎么操作
		List<Word> oldWords = wordDao.rememberOld(userId, oldLimit);
		uniqueWord.addAll(oldWords);

		// 错误率在15%以上的单词
		List<Word> wrongWords = wordDao.rememberWrong(userId, total - uniqueWord.size());
		uniqueWord.addAll(wrongWords);

		// 总共记忆次数小于3次的单词
		if(uniqueWord.size() < total) {
			List<Word> reviewWords = wordDao.rememberReview(userId, total - uniqueWord.size());
			uniqueWord.addAll(reviewWords);
		}

		logger.info("list word, old: {} , wrong: {}, lessMemory: {}",
				oldWords.size(), wrongWords.size(), uniqueWord.size() - oldWords.size() - wrongWords.size());

		return uniqueWord.stream().collect(Collectors.toList());
	}

}
