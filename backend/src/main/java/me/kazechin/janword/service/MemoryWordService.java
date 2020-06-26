package me.kazechin.janword.service;

import me.kazechin.janword.dao.MemoryDetailDao;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.dao.MemoryingCache;
import me.kazechin.janword.model.MemoryingStatus;
import me.kazechin.janword.model.Type;
import me.kazechin.janword.model.Word;
import me.kazechin.janword.dao.WordDao;
import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemoryWordService extends AbstractMemoryService<Word> {

	private final WordDao wordDao;

	@Autowired
	public MemoryWordService(MemoryDetailDao memoryDetailDao,
							 MemoryRecordDao memoryRecordDao,
							 MemoryingCache memoryingCache,
							 WordDao wordDao,
							 MemoryWordFinder memoryWordFinder) {
		super(memoryDetailDao, memoryRecordDao, memoryingCache, memoryWordFinder, Type.WORD);
		this.wordDao = wordDao;
	}

	@Override
	protected boolean isRemembered(MemoryingStatus memoryingStatus) {
		return memoryingStatus.getContinuousCorrectCount() >= 2;
	}

	public Map<String, Object> progress(UserInfo user) {
		Map<String, Object> res = new HashMap<>();

		res.put("count", wordDao.count());
		res.put("countRemember", wordDao.countRemember(user.getUserId()));

		return res;
	}
}

@Component
class MemoryWordFinder implements MemoryFinder<Word> {

	private final WordDao wordDao;
	private final SentenceService sentenceService;


	@Autowired
	public MemoryWordFinder(WordDao wordDao,
							SentenceService sentenceService) {
		this.wordDao = wordDao;
		this.sentenceService = sentenceService;
	}

	@Override
	public Word get(Integer id) {
		return wordDao.get(id);
	}

	@Override
	public Integer getId(Word object) {
		return object.getId();
	}

	@Override
	public List<Word> doAfterList(List<Word> list) {
		return sentenceService.wrap(list);
	}

	@Override
	public List<Word> listNews(int userId, int limit) {
		return wordDao.rememberNew(userId, limit);
	}

	@Override
	public List<Word> listOlds(int userId, int limit) {
		return wordDao.rememberOld(userId, limit);
	}

	@Override
	public List<Word> listWrongs(int userId, int limit) {
		return wordDao.rememberWrong(userId, limit);
	}

	@Override
	public List<Word> listRests(int userId, int limit) {
		return wordDao.rememberReview(userId, limit);
	}
}



