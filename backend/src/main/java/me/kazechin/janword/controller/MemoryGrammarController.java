package me.kazechin.janword.controller;

import me.kazechin.janword.model.Grammar;
import me.kazechin.janword.model.MemoryLimit;
import me.kazechin.janword.service.MemoryGrammarService;
import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 记忆语法 API
 */
@CrossOrigin
@RestController
public class MemoryGrammarController implements MemoryControllerInter {

	private MemoryGrammarService memoryGrammarService;

	@Autowired
	public MemoryGrammarController(MemoryGrammarService memoryGrammarService) {
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

		List<Grammar> grammars = memoryGrammarService.list(userId, new MemoryLimit(10, 10, 10));

		Map<String, Object> res = new HashMap<>();

		// TODO 进度情况
		res.put("datasource", grammars);
		res.put("countRemember", 0);
		res.put("count", 1);

		return res;
	}

	@PostMapping("/card/grammar/finish")
	@Override
	public void finish(@AuthenticationPrincipal UserInfo user) {
		int userId = user.getUserId();
		memoryGrammarService.finish(userId);
	}


	@PatchMapping("/card/grammar/remeber/{id}")
	public boolean remember(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		return memoryGrammarService.correct(userId, id);
	}


	@PatchMapping("/card/grammar/forget/{id}")
	@Override
	public void forget(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		memoryGrammarService.wrong(userId, id);
	}


}
