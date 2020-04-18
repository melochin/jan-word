package me.kazechin.janword.card;

import me.kazechin.janword.extra.kuromoji.KuromojiService;
import me.kazechin.janword.grammar.Grammar;
import me.kazechin.janword.grammar.GrammarDao;
import me.kazechin.janword.grammar.Sentence;
import me.kazechin.janword.grammar.SentenceDao;
import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class MemoryGrammarController implements MemoryControllerInter {

	private MemoryCache memoryCache;

	private GrammarDao grammarDao;

	private SentenceDao sentenceDao;

	private MemoryRecordDao memoryRecordDao;

	private KuromojiService kuromojiService;

	private MemoryDetailDao memoryDetailDao;

	private MemoryGrammarService memoryGrammarService;

	@Autowired
	public MemoryGrammarController(MemoryCache memoryCache,
								   GrammarDao grammarDao,
								   SentenceDao sentenceDao,
								   MemoryRecordDao memoryRecordDao,
								   KuromojiService kuromojiService,
								   MemoryDetailDao memoryDetailDao,
								   MemoryGrammarService memoryGrammarService) {
		this.memoryCache = memoryCache;
		this.grammarDao = grammarDao;
		this.sentenceDao = sentenceDao;
		this.memoryRecordDao = memoryRecordDao;
		this.kuromojiService = kuromojiService;
		this.memoryDetailDao = memoryDetailDao;
		this.memoryGrammarService = memoryGrammarService;
	}


	/**
	 * 返回当前需要记忆的单词
	 *
	 * @return
	 */
	@GetMapping("/card/grammars")
	public Map<String, Object> list(@AuthenticationPrincipal UserInfo user) {

		int userId = user.getUserId();

		Map<String, Object> res = new HashMap<>();

		List<Grammar> grammars = getGrammars(userId);

		for(Grammar grammar : grammars) {
			for(Sentence sentence : grammar.getSentences()) {
				sentence.setReadings(kuromojiService.getReadings(sentence.getSentence()));
			}
		}

		res.put("datasource", grammars);
		res.put("countRemember", 0);
		res.put("count", 1);

		return res;
	}

	private List<Grammar> getGrammars(int userId) {

		List<Grammar> grammars;

		List<Integer> grammarIdList = memoryCache.needRemeberList(MemoryCache.keyGrammar(userId));
		if (grammarIdList != null) {
			grammars = grammarIdList.stream()
					.map(id -> getGrammar(id))
					.filter(grammar -> grammar != null)
					.collect(toList());
			// 删除缓存
			if (grammars.size() == 0) {
				memoryCache.remove(MemoryCache.keyGrammar(userId));
			}

			return grammars;
		}

		grammars = memoryGrammarService.list(userId);
		grammars.forEach(g -> g.setSentences(sentenceDao.list(g.getId())));

		memoryCache.put(
				MemoryCache.keyGrammar(userId),
				grammars.stream().map(grammar -> grammar.getId())
						.collect(toList()));
		return grammars;
	}

	private Grammar getGrammar(int id) {
		Grammar grammar = grammarDao.get(id);
		if (grammar == null) return null;
		grammar.setSentences(sentenceDao.list(id));
		return grammar;
	}

	@PostMapping("/card/grammar/finish")
	@Override
	public void finish(@AuthenticationPrincipal UserInfo user) {
		int userId = user.getUserId();
		int count = handleMemoryCache(userId);

		memoryRecordDao.add(new MemoryRecord(userId, new Date(), "完成语法 " + count + " 个"));

		memoryCache.remove(MemoryCache.keyGrammar(userId));
	}

	private int handleMemoryCache(Integer userId) {
		Map<String, TempMemory> map = memoryCache.get(MemoryCache.keyGrammar(userId));

		int count = map.entrySet().size();
		for(Map.Entry<String,TempMemory> entry : map.entrySet()) {
			int grammarId = Integer.valueOf(entry.getKey());

			if (memoryDetailDao.modifyLastDate(userId, grammarId, Constants.Type.GRAMMAR, entry.getValue().isWrong()) == 0) {
				memoryDetailDao.add(userId, grammarId, Constants.Type.GRAMMAR, entry.getValue().isWrong());
			}
		}

		memoryCache.remove(MemoryCache.keyGrammar(userId));

		return count;
	}


	@PatchMapping("/card/grammar/remeber/{id}")
	public boolean remember(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		return memoryCache.remeber(MemoryCache.keyGrammar(userId), id) >= 1;
	}


	@PatchMapping("/card/grammar/forget/{id}")
	@Override
	public void forget(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		memoryCache.forget(MemoryCache.keyGrammar(userId), id);
	}

}
