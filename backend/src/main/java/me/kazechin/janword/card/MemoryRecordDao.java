package me.kazechin.janword.card;

import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemoryRecordDao {

	@Insert("insert into memory_record (user_id, memory_date, detail) values(#{userId}, #{date}, #{detail})")
	void add(MemoryRecord memoryRecord);

//	@Select("select user_id as userId, memory_date as date, times as times" +
//			" from memory_record where user_id = #{userId} and memory_date = date(#{date})")
//	MemoryRecord get(@Param("userId") int userId, @Param("date") Date date);

	@Select("select date(memory_date) as date, count(*) as count from memory_record " +
			"where memory_date >= #{start} and " +
			"memory_date <= #{end} and user_id = #{userId} " +
			"group by date(memory_date)")
	List<Map> groupByUserIdAndDate(@Param("userId") int userId,
												   @Param("start") LocalDate start,
												   @Param("end") LocalDate date);

	@Select("select memory_date as date, detail as detail from memory_record " +
			"where user_id = #{userId} and date(memory_date) = date(#{date})")
	List<MemoryRecord> list(@Param("userId") int userId, @Param("date") Date date);
}
