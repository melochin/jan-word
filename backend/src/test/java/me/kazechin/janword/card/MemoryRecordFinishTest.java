package me.kazechin.janword.card;

import me.kazechin.janword.word.WordDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MemoryRecordFinishTest {

	private WordDao wordDao = mock(WordDao.class);

	private MemoryCache memoryCache = mock(MemoryCache.class);

	private MemoryDao memoryDao = mock(MemoryDao.class);

	private MermoryRecordDao mermoryRecordDao = mock(MermoryRecordDao.class);

	@Test
	void finishTest() {
		int userId = 0;

		when(memoryCache.list(MemoryCache.keyWord(userId))).thenReturn(new HashSet<>());
		when(mermoryRecordDao.get(eq(userId), any())).thenReturn(null);

		MemoryWordController memoryWordController = new MemoryWordController(wordDao, memoryCache, memoryDao, mermoryRecordDao);
		memoryWordController.finish(userId);

		// 移除缓存
		Mockito.verify(memoryCache, Mockito.times(1))
				.remove(MemoryCache.keyWord(userId));
		// 验证是否查询记忆记录
		Mockito.verify(mermoryRecordDao, Mockito.times(1))
				.get(eq(userId), any());

		// 验证记忆记录是否新增
		Mockito.verify(mermoryRecordDao, Mockito.times(1))
				.add(any());

		reset(mermoryRecordDao);
		MemoryRecord memoryRecord = new MemoryRecord(userId, new Date(), 1);
		when(mermoryRecordDao.get(eq(userId), any())).thenReturn(memoryRecord);
		memoryWordController.finish(userId);

		// 验证记录是否被修改
		assertTrue(memoryRecord.getTimes() == 2);
		Mockito.verify(mermoryRecordDao, Mockito.times(1))
				.modify(any());
	}

}