package me.kazechin.janword.dao;

import me.kazechin.janword.model.MemoryingStatus;
import me.kazechin.janword.model.Type;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * 缓存操作：
 * 缓存正在记忆中的单词ID或者语法ID
 */
@Repository
public class MemoryingCache {

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, MemoryingStatus> hashOps;

	private final int expiredDays = 1;

	public void put(String key, List<Integer> objectIds) {

		remove(key);

		Map<String, MemoryingStatus> statusMap = objectIds.stream()
				.collect(toMap(String::valueOf, id -> new MemoryingStatus()));

		hashOps.putAll(key, statusMap);

		hashOps.getOperations().expire(key, expiredDays, TimeUnit.DAYS);
	}

	public Map<String, MemoryingStatus> get(String key) {
		return hashOps.entries(key);
	}

	public Set<Integer> list(String key) {
		return hashOps.entries(key).keySet()
				.stream()
				.map(s -> Integer.valueOf(s))
				.collect(toSet());
	}

	public void remove(String key) {
		hashOps.getOperations().delete(key);
	}

	public List<Integer> needRemeberList(String key) {
		List<Integer> result = new ArrayList<>();
		Map<String, MemoryingStatus> map = hashOps.entries(key);
		if (map.isEmpty()) return null;

		for (Map.Entry<String, MemoryingStatus> entry : map.entrySet()) {
			if (entry.getValue().getCorrectCount() < 2) {
				result.add(Integer.valueOf(entry.getKey()));
			}
		}

		return result;
	}

	public List<Integer> listObjectIds(String key, Predicate<MemoryingStatus> predicate) {
		Map<String, MemoryingStatus> map = get(key);
		if (map == null || map.isEmpty()) return new ArrayList<>();

		List<Integer> result = new ArrayList<>();

		for (Map.Entry<String, MemoryingStatus> entry : map.entrySet()) {
			if (predicate.test(entry.getValue())) {
				result.add(Integer.valueOf(entry.getKey()));
			}
		}

		return result;
	}

	public MemoryingStatus get(String key, int id) {
		return hashOps.get(key, String.valueOf(id));
	}

	public MemoryingStatus remeber(String key, int id) {
		MemoryingStatus memoryingStatus = hashOps.get(key, String.valueOf(id));
		memoryingStatus.setCorrectCount(memoryingStatus.getCorrectCount() + 1);
		memoryingStatus.setContinuousCorrectCount(memoryingStatus.getContinuousCorrectCount() + 1);
		hashOps.put(key, String.valueOf(id), memoryingStatus);
		return memoryingStatus;
	}

	public void forget(String key, int id) {
		MemoryingStatus memoryingStatus = hashOps.get(key, String.valueOf(id));
		memoryingStatus.setContinuousCorrectCount(0);
		memoryingStatus.setWrongCount(memoryingStatus.getWrongCount() + 1);
		hashOps.put(key, String.valueOf(id), memoryingStatus);
	}

	public static String key(Type type, int userId) {
		switch (type) {
			case WORD:
				return keyWord(userId);
			case GRAMMAR:
				return keyGrammar(userId);
			default:
				throw new RuntimeException("wrong key");
		}
	}

	private static String keyWord(int userId) {
		return "user:memory:word:" + userId;
	}

	private static String keyGrammar(int userId) {
		return "user:memory:grammar:" + userId;
	}

}
