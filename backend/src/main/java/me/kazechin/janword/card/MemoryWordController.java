package me.kazechin.janword.card;

import me.kazechin.janword.user.UserInfo;
import me.kazechin.janword.word.Word;
import me.kazechin.janword.word.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class MemoryWordController implements MemoryControllerInter {

	private WordDao wordDao;

	private MemoryCache memoryCache;

	private MemoryDetailDao memoryDetailDao;

	private MemoryRecordDao memoryRecordDao;

	private MemoryWordService memoryWordService;

	private static int limit = 20;

	@Autowired
	public MemoryWordController(WordDao wordDao,
								MemoryCache memoryCache,
								MemoryDetailDao memoryDetailDao,
								MemoryRecordDao memoryRecordDao,
								MemoryWordService memoryWordService) {
		this.wordDao = wordDao;
		this.memoryCache = memoryCache;
		this.memoryDetailDao = memoryDetailDao;
		this.memoryRecordDao = memoryRecordDao;
		this.memoryWordService = memoryWordService;
	}

	@GetMapping("/card/words")
	@Override
	public Map<String, Object> list(@AuthenticationPrincipal UserInfo user) {
		Map<String, Object> res = new HashMap<>();

		List<Word> words = getWords(user.getUserId());
		res.put("datasource", words);
		res.put("count", wordDao.count());
		res.put("countRemember", wordDao.countRemember(user.getUserId()));

		return res;
	}

	private List<Word> getWords(Integer userId) {

		List<Integer> wordIdList = memoryCache.needRemeberList(MemoryCache.keyWord(userId));
		if (wordIdList != null) {
			return wordIdList.stream()
					.map(id -> wordDao.get(id))
					.filter(word -> word != null)
					.collect(toList());
		}

		List<Word> words = memoryWordService.list(userId, limit);

		memoryCache.put(
				MemoryCache.keyWord(userId),
				words.stream().map(word -> word.getId())
						.collect(toList()));
		return words;
	}

	@PostMapping("/card/word/finish")
	@Override
	public void finish(@AuthenticationPrincipal UserInfo user) {
		int userId = user.getUserId();

		int count = handleMemoryCache(userId);

		memoryRecordDao.add(new MemoryRecord(userId, new Date(), "完成单词 " + count + " 个"));
	}

	private int handleMemoryCache(Integer userId) {
		Map<String, TempMemory> map = memoryCache.get(MemoryCache.keyWord(userId));

		int count = map.entrySet().size();
		for(Map.Entry<String,TempMemory> entry : map.entrySet()) {
			int wordId = Integer.valueOf(entry.getKey());

			if (memoryDetailDao.modifyLastDate(userId, wordId, 0, entry.getValue().isWrong()) == 0) {
				memoryDetailDao.add(userId, wordId, 0, entry.getValue().isWrong());
			}
		}

		memoryCache.remove(MemoryCache.keyWord(userId));

		return count;
	}

	@PatchMapping("/card/word/remeber/{id}")
	@Override
	public boolean remember(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		return memoryCache.remeber(MemoryCache.keyWord(userId), id) >= 2;
	}

	@PatchMapping("/card/word/forget/{id}")
	@Override
	public void forget(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		memoryCache.forget(MemoryCache.keyWord(userId), id);
	}

}
