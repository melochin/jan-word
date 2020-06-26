package me.kazechin.janword.service;

import me.kazechin.janword.dao.MemoryDetailDao;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.dao.MemoryingCache;
import me.kazechin.janword.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class AbstractMemoryService<T> implements MemoryService {

	private final Logger logger = LoggerFactory.getLogger(AbstractMemoryService.class);

	private final MemoryDetailDao memoryDetailDao;
	private final MemoryRecordDao memoryRecordDao;
	private final MemoryFinder<T> memoryFinder;
	private final MemoryingCache memoryingCache;
	private final Type type;

	public AbstractMemoryService(MemoryDetailDao memoryDetailDao,
								 MemoryRecordDao memoryRecordDao,
								 MemoryingCache memoryingCache,
								 MemoryFinder<T> memoryFinder,
								 Type type) {
		this.memoryDetailDao = memoryDetailDao;
		this.memoryRecordDao = memoryRecordDao;
		this.memoryingCache = memoryingCache;
		this.memoryFinder = memoryFinder;
		this.type = type;
	}

	@Override
	@NonNull
	public List<T> list(int userId, MemoryLimit memoryLimit) {

		String key = MemoryingCache.key(type, userId);

		List<T> listFromCache = listFromCache(key);

		if (listFromCache != null && listFromCache.size() > 0) {
			return memoryFinder.doAfterList(listFromCache);
		}
		memoryingCache.remove(key);

		int total = memoryLimit.getNewLimit() + memoryLimit.getOldLimit() + memoryLimit.getWrongLimit();

		Set<T> uniqueSet = new HashSet<>();

		List<T> news = memoryFinder.listNews(userId, memoryLimit.getNewLimit());
		uniqueSet.addAll(news);

		List<T> olds = memoryFinder.listOlds(userId, memoryLimit.getOldLimit());
		uniqueSet.addAll(olds);

		List<T> wrongs = memoryFinder.listWrongs(userId, memoryLimit.getWrongLimit());
		uniqueSet.addAll(wrongs);


		List<T> rests = memoryFinder.listRests(userId, total - uniqueSet.size());
		uniqueSet.addAll(rests);

		logger.info("{} is memorying, where number of new {}, old {} , wrong {}, rest {}.",
				type, news.size(), olds.size(), wrongs.size(), rests.size());

		ArrayList<T> result = new ArrayList<>(uniqueSet);
		Collections.shuffle(result);

		List<Integer> cacheIds = result.stream().map(memoryFinder::getId).collect(toList());
		memoryingCache.put(key, cacheIds);

		return memoryFinder.doAfterList(result);
	}

	private List<T> listFromCache(String key) {
		List<Integer> ids = memoryingCache.listObjectIds(key,
				memoryingStatus -> !this.isRemembered(memoryingStatus));

		if (ids == null || ids.size() == 0) {
			memoryingCache.remove(key);
			return null;
		}

		return ids.stream()
				.map(memoryFinder::get)
				.filter(object -> object != null)
				.collect(toList());
	}

	@Override
	public boolean correct(int userId, int objectId) {
		String key = MemoryingCache.key(type, userId);
		// 避免接口被恶意调用，提前判断一次
		MemoryingStatus memoryingStatus = memoryingCache.get(key, objectId);
		if (isRemembered(memoryingStatus)) return true;

		memoryingStatus = memoryingCache.remeber(key, objectId);
		return isRemembered(memoryingStatus);
	}

	@Override
	public void wrong(int userId, int objectId) {
		String key = MemoryingCache.key(type, userId);
		// 避免接口被恶意调用，提前判断一次
		MemoryingStatus memoryingStatus = memoryingCache.get(key, objectId);
		if (isRemembered(memoryingStatus)) return;

		memoryingCache.forget(key, objectId);
	}

	/**
	 * 通过
	 * @see MemoryingStatus
	 * 判断是否还需要记忆
	 *
	 * @param memoryingStatus
	 * @return
	 */
	protected abstract boolean isRemembered(MemoryingStatus memoryingStatus);

	@Override
	public void finish(int userId) {
		Map<String, MemoryingStatus> map = memoryingCache.get(MemoryingCache.key(type, userId));

		int count = map.entrySet().size();
		for (Map.Entry<String, MemoryingStatus> entry : map.entrySet()) {
			int objectId = Integer.valueOf(entry.getKey());

			if (memoryDetailDao.modifyLastDate(userId, objectId, type.getValue(), entry.getValue().isWrong()) == 0) {
				memoryDetailDao.add(userId, objectId, type.getValue(), entry.getValue().isWrong());
			}
		}

		memoryRecordDao.add(new MemoryRecord(userId, new Date(), "完成 " + type.toString() + " " + count + " 个"));
		memoryingCache.remove(MemoryingCache.key(type, userId));
	}


}