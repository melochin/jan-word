package me.kazechin.janword.service;

import org.springframework.lang.NonNull;

import java.util.List;

public interface MemoryFinder<T> {

	T get(Integer id);

	Integer getId(T object);

	@NonNull
	List<T> listNews(int userId, int limit);

	@NonNull
	List<T> listOlds(int userId, int limit);

	@NonNull
	List<T> listWrongs(int userId, int limit);

	@NonNull
	List<T> listRests(int userId, int limit);

	List<T> doAfterList(List<T> list);
}
