package me.kazechin.janword.card;

import me.kazechin.janword.user.UserInfo;
import me.kazechin.janword.word.WordDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemoryRecordFinishTest {

	private WordDao wordDao = mock(WordDao.class);

	private MemoryCache memoryCache = mock(MemoryCache.class);

	private MemoryDao memoryDao = mock(MemoryDao.class);

	private MemoryRecordDao memoryRecordDao = mock(MemoryRecordDao.class);

	private MemoryWordService memoryWordService = mock(MemoryWordService.class);

	@Test
	void finishTest() {
		int userId = 0;

		when(memoryCache.list(MemoryCache.keyWord(userId))).thenReturn(new HashSet<>());

		MemoryWordController memoryWordController = new MemoryWordController(wordDao, memoryCache, memoryDao, memoryRecordDao, memoryWordService);
		memoryWordController.finish(new UserInfo(userId, "0", null));

		// 移除缓存
		Mockito.verify(memoryCache, Mockito.times(1))
				.remove(MemoryCache.keyWord(userId));
		// 验证是否查询记忆记录

		// 验证记忆记录是否新增
		Mockito.verify(memoryRecordDao, Mockito.times(1))
				.add(any());

	}

}