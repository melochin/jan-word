package me.kazechin.janword.dao;

import me.kazechin.janword.model.Grammar;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GrammarDao {

	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
	@Insert("insert into grammar (grammar, detail) values(#{grammar}, #{detail})")
	void add(Grammar grammar);

	@Select("select id, grammar, detail from grammar")
	List<Grammar> list();

	@Select("select id, grammar, detail from grammar where grammar like concat('%', #{keyword}, '%')")
	List<Grammar> listByKeyword(@Param("keyword") String keyword);

	@Delete("delete from grammar where id = #{id}")
	void remove(@Param("id") int id);

	@Update("update grammar set grammar = #{grammar}, detail = #{detail} where id = #{id}")
	void update(Grammar grammar);

	@Select("select id, grammar, detail from grammar where id = #{id}")
	Grammar get(Integer id);

	@Select("select * from grammar where id not in (\n" +
			"\tselect object_id from memory_detail where user_id = #{userId} and type_no = 1 \n" +
			") limit #{limit};")
	List<Grammar> rememberNew(@Param("userId") int userId, @Param("limit") int limit);


	@Select("select * from grammar where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join memory_detail \n" +
			"\t\t\ton word.id= memory_detail.object_id\n" +
			"            where memory_detail.user_id = #{userId} and type_no = 1 \n" +
			"\t\t\t\tand memory_detail.correct / (memory_detail.correct + memory_detail.wrong) < 0.8\n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") ")
	List<Grammar> rememberWrong(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select * from grammar where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join memory_detail \n" +
			"\t\t\ton word.id= memory_detail.object_id\n" +
			"            where memory_detail.user_id = #{userId} and type_no = 1 \n" +
			"\t\t\t\tand (memory_detail.correct + memory_detail.wrong) < 3\n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") \n"
	)
	List<Grammar> rememberReview(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select * from grammar where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join memory_detail \n" +
			"\t\t\ton word.id= memory_detail.object_id\n" +
			"            where memory_detail.user_id = #{userId} and type_no = 1 \n" +
			"\t\t\t\tand memory_detail.last_date < DATE_SUB(NOW(), INTERVAL 7 DAY) \n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") \n" +
			"\n")
	List<Grammar> rememberOld(@Param("userId") int userId, @Param("limit") int limit);
}
