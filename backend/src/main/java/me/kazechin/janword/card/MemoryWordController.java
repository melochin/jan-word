package me.kazechin.janword.card;

import me.kazechin.janword.word.Word;
import me.kazechin.janword.word.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class MemoryWordController implements MemoryControllerInter<Word> {

	private WordDao wordDao;

	private MemoryCache memoryCache;

	private MemoryDao memoryDao;

	private MermoryRecordDao mermoryRecordDao;

	private static int limit = 30;

	@Autowired
	public MemoryWordController(WordDao wordDao,
								MemoryCache memoryCache,
								MemoryDao memoryDao,
								MermoryRecordDao mermoryRecordDao) {
		this.wordDao = wordDao;
		this.memoryCache = memoryCache;
		this.memoryDao = memoryDao;
		this.mermoryRecordDao = mermoryRecordDao;
	}

	@GetMapping("/card/words")
	@Override
	public List<Word> list(Integer userId) {
		userId = 0;

		List<Integer> wordIdList = memoryCache.needRemeberList(MemoryCache.keyWord(userId));
		if (wordIdList != null) {
			return wordIdList.stream()
					.map(id -> wordDao.get(id))
					.filter(word -> word != null)
					.collect(toList());
		}

		List<Word> words = wordDao.remember(limit);

		memoryCache.put(
				MemoryCache.keyWord(userId),
				words.stream().map(word -> word.getId())
						.collect(toList()));
		return words;
	}

	@PostMapping("/card/word/finish")
	@Override
	public void finish(Integer userId) {
		userId = 0;

		handleMemoryCache(userId);

		MemoryRecord memoryRecord = mermoryRecordDao.get(userId, new Date());
		if (memoryRecord == null) {
			mermoryRecordDao.add(new MemoryRecord(userId, new Date(), 1));
		} else {
			memoryRecord.increTimes();
			mermoryRecordDao.modify(memoryRecord);
		}

	}

	private void handleMemoryCache(Integer userId) {
		Map<String, TempMemory> map = memoryCache.get(MemoryCache.keyWord(userId));

		for(Map.Entry<String,TempMemory> entry : map.entrySet()) {
			int wordId = Integer.valueOf(entry.getKey());

			if (memoryDao.modifyLastDate(wordId, entry.getValue().isWrong()) == 0) {
				memoryDao.add(wordId, entry.getValue().isWrong());
			}
		}

		memoryCache.remove(MemoryCache.keyWord(userId));
	}

	@PatchMapping("/card/word/remeber/{id}")
	@Override
	public boolean remember(Integer userId, @PathVariable Integer id) {
		userId = 0;
		return memoryCache.remeber(MemoryCache.keyWord(userId), id) >= 2;
	}

	@PatchMapping("/card/word/forget/{id}")
	@Override
	public void forget(Integer userId, @PathVariable Integer id) {
		userId = 0;
		memoryCache.forget(MemoryCache.keyWord(userId), id);
	}

}
