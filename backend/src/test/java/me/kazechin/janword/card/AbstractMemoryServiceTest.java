package me.kazechin.janword.card;

import me.kazechin.janword.dao.MemoryDetailDao;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.dao.MemoryingCache;
import me.kazechin.janword.model.MemoryLimit;
import me.kazechin.janword.model.MemoryingStatus;
import me.kazechin.janword.model.Type;
import me.kazechin.janword.service.AbstractMemoryService;
import me.kazechin.janword.service.MemoryFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 */
public class AbstractMemoryServiceTest {

	private int userId = 0;

	private MemoryDetailDao memoryDetailDao;
	private MemoryRecordDao memoryRecordDao;
	protected MemoryingCache memoryingCache;

	private AbstractMemoryService<Integer> memoryService;

	@BeforeEach
	public void setup() {

		memoryDetailDao = mock(MemoryDetailDao.class);
		memoryRecordDao = mock(MemoryRecordDao.class);
		memoryingCache = mock(MemoryingCache.class);
	}


	@Test
	public void testListWithoutCacheList() {

		MemoryFinder<Integer> memoryFinder = new MemoryFinder<Integer>() {
			@Override
			public Integer get(Integer id) {
				return 1;
			}

			@Override
			public Integer getId(Integer object) {
				return 1;
			}

			@Override
			public List<Integer> listNews(int userId, int limit) {
				return Arrays.asList(1);
			}

			@Override
			public List<Integer> listOlds(int userId, int limit) {
				return Arrays.asList(2, 3);
			}

			@Override
			public List<Integer> listWrongs(int userId, int limit) {
				return Arrays.asList(4, 5, 6);
			}

			@Override
			public List<Integer> listRests(int userId, int limit) {
				return new ArrayList<>();
			}

			@Override
			public List<Integer> doAfterList(List<Integer> list) {
				return list;
			}
		};

		memoryService = new AbstractMemoryService<Integer>
				(memoryDetailDao, memoryRecordDao, memoryingCache, memoryFinder, Type.GRAMMAR) {

			@Override
			protected boolean isRemembered(MemoryingStatus memoryingStatus) {
				return false;
			}
		};

		MemoryLimit memoryLimit = new MemoryLimit(1, 2, 3);

		assertEquals(6, memoryService.list(userId, memoryLimit).size());
		verify(memoryingCache, times(1)).put(any(), any());
	}


//	@Test
//	public void noRestWithEnoughResourceTest() {
//		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
//		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(2));
//		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(3));
//		when(memorying.rests(userId, 0)).thenReturn(new ArrayList<>());
//
//		assertEquals(3, memoryService.list(userId).size());
//	}
//
//	@Test
//	public void noRestWithNoEnoughResourceTest() {
//		when(memorying.news(userId, newsLimit)).thenReturn(new ArrayList<>());
//		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(1));
//		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
//		when(memorying.rests(userId, newsLimit)).thenReturn(new ArrayList<>());
//
//		assertEquals(2, memoryService.list(userId).size());
//	}
//
//	@Test
//	public void RestWithNoEnoughResourceTest() {
//		when(memorying.news(userId, newsLimit)).thenReturn(new ArrayList<>());
//		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(1));
//		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
//		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));
//
//		assertEquals(3, memoryService.list(userId).size());
//
//		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
//		when(memorying.olds(userId, oldsLimit)).thenReturn(new ArrayList<>());
//		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
//		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));
//		assertEquals(3, memoryService.list(userId).size());
//
//		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
//		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(2));
//		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(new ArrayList<>());
//		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));
//		assertEquals(3, memoryService.list(userId).size());
//	}


}