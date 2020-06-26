package me.kazechin.janword.service;

import me.kazechin.janword.model.MemoryLimit;

import java.util.List;

public interface MemoryService<T> {

	/**
	 * 查询本次需要记忆的对象列表
	 *
	 * @param userId
	 * @return
	 */
	List<T> list(int userId, MemoryLimit memoryLimit);

	/**
	 * 更新该对象的正确次数
	 *
	 * @param userId
	 * @param objectId
	 * @return true, 下次不需要记忆; false, 下次还需要记忆
	 */
	boolean correct(int userId, int objectId);

	/**
	 * 更新对象的错误次数
	 *
	 * @param userId
	 * @param objectId
	 */
	void wrong(int userId, int objectId);

	/**
	 * 结束本次记忆
	 *
	 * @param userId
	 */
	void finish(int userId);

}
