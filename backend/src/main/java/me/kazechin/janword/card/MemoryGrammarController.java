package me.kazechin.janword.card;

import me.kazechin.janword.grammar.Grammar;
import me.kazechin.janword.grammar.GrammarDao;
import me.kazechin.janword.grammar.SentenceDao;
import me.kazechin.janword.word.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class MemoryGrammarController implements MemoryControllerInter<Grammar> {

	@Autowired
	private MemoryCache memoryCache;

	@Autowired
	private GrammarDao grammarDao;

	@Autowired
	private SentenceDao sentenceDao;


	/**
	 * 返回当前需要记忆的单词
	 *
	 * @return
	 */
	@GetMapping("/card/grammars")
	public List<Grammar> list(Integer userId) {
		userId = 0;

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
	public void finish(Integer userId) {
		userId = 0;
		Set<Integer> set = memoryCache.list(MemoryCache.keyGrammar(userId));
		if (set.size() > 0) {
			// TODO update date
		}
		memoryCache.remove(MemoryCache.keyGrammar(userId));
	}

	@PatchMapping("/card/grammar/remeber/{id}")
	public boolean remember(Integer userId, @PathVariable Integer id) {
		userId = 0;
		return memoryCache.remeber(MemoryCache.keyGrammar(userId), id) >= 2;
	}


	@PatchMapping("/card/grammar/forget/{id}")
	@Override
	public void forget(Integer userId, @PathVariable Integer id) {
		userId = 0;
		memoryCache.forget(MemoryCache.keyGrammar(userId), id);
	}

}
