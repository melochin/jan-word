package me.kazechin.janword.card;

import java.util.List;

public interface MemoryControllerInter<T> {

	List<T> list(Integer userId);

	void finish(Integer userId);

	boolean remember(Integer userId, Integer id);

	void forget(Integer userId, Integer id);

}
