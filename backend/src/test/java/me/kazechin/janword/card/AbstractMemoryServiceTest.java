package me.kazechin.janword.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractMemoryServiceTest {

	private int newsLimit = 1;

	private int oldsLimit = 1;

	private int wrongsLimit = 1;

	private int userId = 0;

	private MemoryingInter<Integer> memorying = mock(MemoryingInter.class);

	private AbstractMemoryService<Integer> memoryService;

	@BeforeEach
	public void setup() {

		when(memorying.getNewLimit()).thenReturn(newsLimit);
		when(memorying.getOldLimit()).thenReturn(oldsLimit);
		when(memorying.getWrongLimit()).thenReturn(wrongsLimit);

		this.memoryService = new AbstractMemoryService<Integer>(memorying);
	}

	@Test
	public void noRestWithEnoughResourceTest() {
		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(2));
		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(3));
		when(memorying.rests(userId, 0)).thenReturn(new ArrayList<>());

		assertEquals(3, memoryService.list(userId).size());
	}

	@Test
	public void noRestWithNoEnoughResourceTest() {
		when(memorying.news(userId, newsLimit)).thenReturn(new ArrayList<>());
		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(1));
		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
		when(memorying.rests(userId, newsLimit)).thenReturn(new ArrayList<>());

		assertEquals(2, memoryService.list(userId).size());
	}

	@Test
	public void RestWithNoEnoughResourceTest() {
		when(memorying.news(userId, newsLimit)).thenReturn(new ArrayList<>());
		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(1));
		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));

		assertEquals(3, memoryService.list(userId).size());

		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
		when(memorying.olds(userId, oldsLimit)).thenReturn(new ArrayList<>());
		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(Arrays.asList(2));
		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));
		assertEquals(3, memoryService.list(userId).size());

		when(memorying.news(userId, newsLimit)).thenReturn(Arrays.asList(1));
		when(memorying.olds(userId, oldsLimit)).thenReturn(Arrays.asList(2));
		when(memorying.wrongs(userId, wrongsLimit)).thenReturn(new ArrayList<>());
		when(memorying.rests(userId, newsLimit)).thenReturn(Arrays.asList(3));
		assertEquals(3, memoryService.list(userId).size());
	}


}