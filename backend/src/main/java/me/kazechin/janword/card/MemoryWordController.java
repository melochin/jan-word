package me.kazechin.janword.card;

import me.kazechin.janword.word.Word;
import me.kazechin.janword.word.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class MemoryWordController implements MemoryControllerInter<Word>{

	@Autowired
	private WordDao wordDao;

	@Autowired
	private MemoryCache memoryCache;

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

		List<Word> words = wordDao.remember();
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
		Set<Integer> set = memoryCache.list(MemoryCache.keyWord(userId));
		if (set.size() > 0) {
			// TODO update date
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
