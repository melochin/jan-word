package me.kazechin.janword.card;

import me.kazechin.janword.word.Word;
import me.kazechin.janword.word.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemoryWordService extends AbstractMemoryService<Word> {

	@Resource(type= MemoryingWord.class)
	public void setMemoryService(MemoryingInter<Word> memoryService) {
		this.memoryService = memoryService;
	}

}

@Service
class MemoryingWord implements MemoryingInter<Word> {

	@Autowired
	private WordDao wordDao;

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
	public List<Word> news(int userId, int limit) {
		return wordDao.rememberNew(userId, limit);
	}

	@Override
	public List<Word> wrongs(int userId, int limit) {
		return wordDao.rememberWrong(userId, limit);
	}

	@Override
	public List<Word> olds(int userId, int limit) {
		return wordDao.rememberOld(userId, limit);
	}

	@Override
	public List<Word> rests(int userId, int limit) {
		return wordDao.rememberReview(userId, limit);
	}
}
