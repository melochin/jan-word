package me.kazechin.janword.card;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MemoryRecord {

	private int userId;

	private Date date;

	private String detail;

	public MemoryRecord() {}

	public MemoryRecord(int userId, Date date, String detail) {
		this.userId = userId;
		this.date = date;
		this.detail = detail;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
