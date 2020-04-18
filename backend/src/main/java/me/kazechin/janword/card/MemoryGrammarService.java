package me.kazechin.janword.card;

import me.kazechin.janword.grammar.Grammar;
import me.kazechin.janword.grammar.GrammarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryGrammarService extends AbstractMemoryService<Grammar> {

	@Resource(type= MemoringGrammar.class)
	public void setMemoryService(MemoryingInter<Grammar> memoryService) {
		this.memoryService = memoryService;
	}

}

@Service
class MemoringGrammar implements MemoryingInter<Grammar> {

	@Autowired
	private GrammarDao grammarDao;

	@Override
	public int getNewLimit() {
		return 10;
	}

	@Override
	public int getOldLimit() {
		return 10;
	}

	@Override
	public int getWrongLimit() {
		return 10;
	}

	@Override
	public List<Grammar> news(int userId, int limit) {
		return grammarDao.rememberNew(userId, limit);
	}

	@Override
	public List<Grammar> wrongs(int userId, int limit) {
		return grammarDao.rememberWrong(userId, limit);
	}

	@Override
	public List<Grammar> olds(int userId, int limit) {
		return grammarDao.rememberOld(userId, limit);
	}

	@Override
	public List<Grammar> rests(int userId, int limit) {
		return grammarDao.rememberReview(userId, limit);
	}
}
