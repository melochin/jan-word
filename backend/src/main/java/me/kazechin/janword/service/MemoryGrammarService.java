package me.kazechin.janword.service;

import me.kazechin.janword.dao.MemoryDetailDao;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.dao.MemoryingCache;
import me.kazechin.janword.model.MemoryingStatus;
import me.kazechin.janword.model.Grammar;
import me.kazechin.janword.dao.GrammarDao;
import me.kazechin.janword.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryGrammarService extends AbstractMemoryService<Grammar> {

	@Autowired
	public MemoryGrammarService(GrammarDao grammarDao,
								SentenceService sentenceService,
								MemoryRecordDao memoryRecordDao,
								MemoryDetailDao memoryDetailDao,
								MemoryingCache memoryingCache,
								MemoryGrammarFinder memoryGrammarFinder) {
		super(memoryDetailDao, memoryRecordDao, memoryingCache, memoryGrammarFinder, Type.GRAMMAR);
	}

	@Override
	protected boolean isRemembered(MemoryingStatus memoryingStatus) {
		return memoryingStatus.getContinuousCorrectCount() >= 1;
	}


}

@Component
class MemoryGrammarFinder implements MemoryFinder<Grammar> {

	private final GrammarDao grammarDao;
	private final SentenceService sentenceService;

	@Autowired
	MemoryGrammarFinder(GrammarDao grammarDao, SentenceService sentenceService) {
		this.grammarDao = grammarDao;
		this.sentenceService = sentenceService;
	}

	@Override
	public Grammar get(Integer id) {
		return grammarDao.get(id);
	}

	@Override
	public Integer getId(Grammar object) {
		return object.getId();
	}

	@Override
	public List<Grammar> doAfterList(List<Grammar> list) {
		return sentenceService.wrap(list);
	}

	@Override
	public List<Grammar> listNews(int userId, int limit) {
		return grammarDao.rememberNew(userId, limit);
	}

	@Override
	public List<Grammar> listOlds(int userId, int limit) {
		return grammarDao.rememberOld(userId, limit);
	}

	@Override
	public List<Grammar> listWrongs(int userId, int limit) {
		return grammarDao.rememberWrong(userId, limit);
	}

	@Override
	public List<Grammar> listRests(int userId, int limit) {
		return grammarDao.rememberReview(userId, limit);
	}

}