package me.kazechin.janword.card;

import java.util.List;

public interface MemoryingInter<T> {

	int getNewLimit();

	int getOldLimit();

	int getWrongLimit();

	List<T> news(int userId, int limit);

	List<T> wrongs(int userId, int limit);

	List<T> olds(int userId, int limit);

	List<T> rests(int userId, int limit);
}
