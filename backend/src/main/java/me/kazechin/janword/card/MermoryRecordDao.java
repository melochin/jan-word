package me.kazechin.janword.card;

import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface MermoryRecordDao {

	@Insert("insert into memory_record (user_id, memory_date, times) values(#{userId}, #{date}, #{times})")
	void add(MemoryRecord memoryRecord);

	@Select("select user_id as userId, memory_date as date, times as times" +
			" from memory_record where user_id = #{userId} and memory_date = date(#{date})")
	MemoryRecord get(@Param("userId") int userId, @Param("date") Date date);

	@Update("update memory_record set times = #{times} where user_id =#{userId} and memory_date = date(#{date});")
	void modify(MemoryRecord memoryRecord);
}
