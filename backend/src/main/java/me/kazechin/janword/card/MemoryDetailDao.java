package me.kazechin.janword.card;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemoryDetailDao {

	@Update("<script>" +
			"update memory_detail set last_date = now(), " +
			"<when test='isWrong'> wrong = wrong + 1 " +
			"</when>" +
			"<when test='isWrong == false'> correct = correct + 1 " +
			"</when>" +
			"where object_id = #{objectId} and user_id = #{userId} and type_no = #{type}" +
			"</script>")
	int modifyLastDate(@Param("userId") int userId,
					   @Param("objectId") int objectId,
					   @Param("type") int type,
					   @Param("isWrong") boolean isWrong);

	@Insert("<script>" +
			"insert into memory_detail (last_date, user_id, object_id, type_no, wrong, correct) " +
			"values(now(), #{userId}, #{objectId}, #{type}, " +
			"<when test='isWrong'> 1, 0 " +
			"</when>" +
			"<when test='isWrong == false'> 0, 1 " +
			"</when>" +
			")" +
			"</script>")
	void add(@Param("userId") int userId,
			 @Param("objectId") int objectId,
			 @Param("type") int type,
			 @Param("isWrong") boolean isWrong);


}
