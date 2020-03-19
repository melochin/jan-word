package me.kazechin.janword.card;

import java.util.Date;

public class MemoryRecord {

	private int userId;

	private Date date;

	private int times;

	public MemoryRecord(int userId, Date date, int times) {
		this.userId = userId;
		this.date = date;
		this.times = times;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int increTimes() {
		this.times += 1;
		return times;
	}
}
