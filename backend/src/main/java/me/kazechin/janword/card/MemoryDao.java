package me.kazechin.janword.card;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemoryDao {

	// TODO isWrong
	@Update("update word_remember set last_date = now() where word_id = #{wordId}")
	int modifyLastDate(@Param("wordId") int wordId, @Param("isWrong") boolean isWrong);

	@Insert("insert into word_remember (last_date, word_id) values(now(), #{wordId})")
	void add(@Param("wordId") int wordId, @Param("isWrong") boolean isWrong);


}
