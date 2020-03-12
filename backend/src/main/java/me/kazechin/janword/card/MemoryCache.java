package me.kazechin.janword.card;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Repository
public class MemoryCache {

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Integer> hashOps;

	public void put(String key, List<Integer> idList) {
		for (int id : idList) {
			hashOps.put(key, String.valueOf(id), 0);
		}
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
		Map<String, Integer> map = hashOps.entries(key);
		if (map.isEmpty()) return null;

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() < 2) {
				result.add(Integer.valueOf(entry.getKey()));
			}
		}

		return result;
	}


	public int remeber(String key, int id) {
		Integer count = hashOps.get(key, String.valueOf(id));
		hashOps.put(key, String.valueOf(id), count + 1);
		return count + 1;
	}

	public void forget(String key, int id) {
		hashOps.put(key, String.valueOf(id), 0);
	}

	public static String keyWord(int userId) {
		return "user:memory:word:" + userId;
	}

	public static String keyGrammar(int userId) {
		return "user:memory:grammar:" + userId;
	}

}
