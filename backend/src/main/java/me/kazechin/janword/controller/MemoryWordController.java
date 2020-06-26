package me.kazechin.janword.controller;

import me.kazechin.janword.model.MemoryLimit;
import me.kazechin.janword.service.MemoryWordService;
import me.kazechin.janword.user.UserInfo;
import me.kazechin.janword.model.Word;
import me.kazechin.janword.dao.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记忆单词 API
 */
@CrossOrigin
@RestController
public class MemoryWordController implements MemoryControllerInter {

	private MemoryWordService memoryWordService;

	@Autowired
	public MemoryWordController(MemoryWordService memoryWordService) {
		this.memoryWordService = memoryWordService;
	}

	/**
	 * 读取用户当前需要记忆的单词
	 *
	 * @param user
	 * @return
	 */
	@GetMapping("/card/words")
	@Override
	public Map<String, Object> list(@AuthenticationPrincipal UserInfo user) {
		Map<String, Object> res = new HashMap<>();

		List<Word> words = memoryWordService.list(user.getUserId(), new MemoryLimit(10, 10, 10));
		res.put("datasource", words);
		res.putAll(memoryWordService.progress(user));
		return res;
	}

	/**
	 * 结束本次记忆
	 *
	 * @param user
	 */
	@PostMapping("/card/word/finish")
	@Override
	public void finish(@AuthenticationPrincipal UserInfo user) {
		int userId = user.getUserId();
		memoryWordService.finish(userId);
	}

	/**
	 * 记得某个单词
	 *
	 * @param user
	 * @param id
	 * @return
	 */
	@PatchMapping("/card/word/remeber/{id}")
	@Override
	public boolean remember(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		return memoryWordService.correct(userId, id);
	}

	/**
	 * 忘记某个单词
	 *
	 * @param user
	 * @param id
	 */
	@PatchMapping("/card/word/forget/{id}")
	@Override
	public void forget(@AuthenticationPrincipal UserInfo user, @PathVariable Integer id) {
		int userId = user.getUserId();
		memoryWordService.wrong(userId, id);
	}

}
