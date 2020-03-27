package me.kazechin.janword.card;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toSet;

/**
 * 缓存操作：
 * 缓存正在记忆中的单词ID或者语法ID
 */
@Repository
public class MemoryCache {

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, TempMemory> hashOps;

	public void put(String key, List<Integer> idList) {
		for (int id : idList) {
			hashOps.put(key, String.valueOf(id), new TempMemory());
		}
		hashOps.getOperations().expire(key, 1, TimeUnit.DAYS);
	}

	public Map<String, TempMemory> get(String key) {
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
		return;
	}


	public List<Integer> needRemeberList(String key) {
		List<Integer> result = new ArrayList<>();
		Map<String, TempMemory> map = hashOps.entries(key);
		if (map.isEmpty()) return null;

		for (Map.Entry<String, TempMemory> entry : map.entrySet()) {
			if (entry.getValue().getRight() < 2) {
				result.add(Integer.valueOf(entry.getKey()));
			}
		}

		return result;
	}


	public int remeber(String key, int id) {
		TempMemory tempMemory = hashOps.get(key, String.valueOf(id));
		tempMemory.setRight(tempMemory.getRight() + 1);
		hashOps.put(key, String.valueOf(id), tempMemory);
		return tempMemory.getRight();
	}

	public void forget(String key, int id) {
		TempMemory tempMemory = hashOps.get(key, String.valueOf(id));
		tempMemory.setWrong(true);
		tempMemory.setRight(0);
		hashOps.put(key, String.valueOf(id), tempMemory);
	}

	public static String keyWord(int userId) {
		return "user:memory:word:" + userId;
	}

	public static String keyGrammar(int userId) {
		return "user:memory:grammar:" + userId;
	}

}
