package me.kazechin.janword.card;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractMemoryService<T> {

	private final Logger logger = LoggerFactory.getLogger(AbstractMemoryService.class);

	protected MemoryingInter<T> memoryService;

	public AbstractMemoryService() {
	}

	public AbstractMemoryService(MemoryingInter<T> memoryService) {
		this.memoryService = memoryService;
	}

	public List<T> list(int userId) {

		int total = memoryService.getNewLimit() + memoryService.getOldLimit() + memoryService.getWrongLimit();

		Set<T> uniqueSet = new HashSet<>();

		List<T> news = memoryService.news(userId, memoryService.getNewLimit());
		uniqueSet.addAll(news);

		List<T> olds = memoryService.olds(userId, memoryService.getOldLimit());
		uniqueSet.addAll(olds);

		List<T> wrongs = memoryService.wrongs(userId, memoryService.getWrongLimit());
		uniqueSet.addAll(wrongs);


		List<T> rests = memoryService.rests(userId, total - uniqueSet.size());
		uniqueSet.addAll(rests);

		logger.info("need memorying words, new:{}, old: {} , wrong: {}, rest: {}",
				news.size(), olds.size(), wrongs.size(), rests.size());

		ArrayList<T> result = new ArrayList<>(uniqueSet);
		Collections.shuffle(result);
		return result;
	}

}