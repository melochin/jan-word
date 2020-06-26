package me.kazechin.janword.card;

import me.kazechin.janword.dao.MemoryDetailDao;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.dao.MemoryingCache;
import me.kazechin.janword.service.MemoryWordService;
import me.kazechin.janword.dao.WordDao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemoryRecordFinishTest {

	private WordDao wordDao = mock(WordDao.class);

	private MemoryingCache memoryingCache = mock(MemoryingCache.class);

	private MemoryDetailDao memoryDetailDao = mock(MemoryDetailDao.class);

	private MemoryRecordDao memoryRecordDao = mock(MemoryRecordDao.class);

	private MemoryWordService memoryWordService = mock(MemoryWordService.class);

//	@Test
//	void finishTest() {
//		int userId = 0;
//
//		when(memoryingCache.list(MemoryingCache.keyWord(userId))).thenReturn(new HashSet<>());
//
//		MemoryWordController memoryWordController = new MemoryWordController(wordDao, memoryingCache, memoryDetailDao, memoryRecordDao, memoryWordService);
//		memoryWordController.finish(new UserInfo(userId, "0", null));
//
//		// 移除缓存
//		Mockito.verify(memoryingCache, Mockito.times(1))
//				.remove(MemoryingCache.keyWord(userId));
//		// 验证是否查询记忆记录
//
//		// 验证记忆记录是否新增
//		Mockito.verify(memoryRecordDao, Mockito.times(1))
//				.add(any());
//
//	}

}