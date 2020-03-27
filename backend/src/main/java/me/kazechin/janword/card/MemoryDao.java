package me.kazechin.janword.card;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemoryDao {

	@Update("<script>" +
			"update word_remember set last_date = now(), " +
			"<when test='isWrong'> wrong = wrong + 1 " +
			"</when>" +
			"<when test='isWrong == false'> correct = correct + 1 " +
			"</when>" +
			"where word_id = #{wordId} and user_id = #{userId}" +
			"</script>")
	int modifyLastDate(@Param("userId") int userId,
					   @Param("wordId") int wordId,
					   @Param("isWrong") boolean isWrong);

	@Insert("<script>" +
			"insert into word_remember (last_date, user_id, word_id, wrong, correct) " +
			"values(now(), #{userId}, #{wordId}, " +
			"<when test='isWrong'> 1, 0 " +
			"</when>" +
			"<when test='isWrong == false'> 0, 1 " +
			"</when>" +
			")" +
			"</script>")
	void add(@Param("userId") int userId,
			 @Param("wordId") int wordId,
			 @Param("isWrong") boolean isWrong);


}
