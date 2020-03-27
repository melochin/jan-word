package me.kazechin.janword.card;

import me.kazechin.janword.grammar.Grammar;
import me.kazechin.janword.grammar.GrammarDao;
import me.kazechin.janword.grammar.SentenceDao;
import me.kazechin.janword.user.UserInfo;
import me.kazechin.janword.word.Word;
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

	@Autowired
	public MemoryGrammarController(MemoryCache memoryCache,
								   GrammarDao grammarDao,
								   SentenceDao sentenceDao,
								   MemoryRecordDao memoryRecordDao) {
		this.memoryCache = memoryCache;
		this.grammarDao = grammarDao;
		this.sentenceDao = sentenceDao;
		this.memoryRecordDao = memoryRecordDao;
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
		res.put("datasource", grammars);
		res.put("countRemember", 0);
		res.put("count", 1);

		return res;

	}

	private List<Grammar> getGrammars(int userId) {
		List<Integer> grammarIdList = memoryCache.needRemeberList(MemoryCache.keyGrammar(userId));
		if (grammarIdList != null) {
			return grammarIdList.stream()
					.map(id -> getGrammar(id))
					.filter(grammar -> grammar != null)
					.collect(toList());
		}

		List<Grammar> grammars = grammarDao.list();
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
		Set<Integer> set = memoryCache.list(MemoryCache.keyGrammar(userId));
		if (set.size() > 0) {
			// TODO update date
		}

		memoryRecordDao.add(new MemoryRecord(userId, new Date(), "完成语法 " + set.size() + " 个"));

		memoryCache.remove(MemoryCache.keyGrammar(userId));
	}

	@PatchMapping("/card/grammar/remeber/{id}")
	public boolean remember(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		return memoryCache.remeber(MemoryCache.keyGrammar(userId), id) >= 2;
	}


	@PatchMapping("/card/grammar/forget/{id}")
	@Override
	public void forget(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		memoryCache.forget(MemoryCache.keyGrammar(userId), id);
	}

}
