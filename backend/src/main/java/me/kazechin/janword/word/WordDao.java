package me.kazechin.janword.word;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WordDao {

	@Select("select * from word;")
	List<Word> list();

	@Insert("insert into word (word, gana, chinese, origin) values(#{word}, #{gana}, #{chinese}, #{origin})")
	void add(Word word);

	@Delete("delete from word where id = #{id}")
	void remove(int id);

	@Update("update word set word=#{word}, gana=#{gana}, chinese=#{chinese}, origin=#{origin} where id=#{id}")
	void modify(Word word);

	@Select("select * from word where id not in (\n" +
			"\tselect word_id from word_remember where user_id = #{userId}\n" +
			") limit #{limit};")
	List<Word> rememberNew(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select * from word where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join word_remember \n" +
			"\t\t\ton word.id= word_remember.word_id\n" +
			"            where word_remember.user_id = #{userId} \n" +
			"\t\t\t\tand word_remember.last_date < DATE_SUB(NOW(), INTERVAL 7 DAY) \n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") \n" +
			"\n")
	List<Word> rememberOld(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select * from word where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join word_remember \n" +
			"\t\t\ton word.id= word_remember.word_id\n" +
			"            where word_remember.user_id = #{userId} \n" +
			"\t\t\t\tand word_remember.correct / (word_remember.correct + word_remember.wrong) < 0.85\n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") ")
	List<Word> rememberWrong(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select * from word where id in (\n" +
			"\tselect * from (\n" +
			"\t\tselect word.id from word left join word_remember \n" +
			"\t\t\ton word.id= word_remember.word_id\n" +
			"            where word_remember.user_id = #{userId} \n" +
			"\t\t\t\tand (word_remember.correct + word_remember.wrong) < 3\n" +
			"\t\t\tlimit #{limit}\n" +
			"    ) as a\n" +
			") \n"
			)
	List<Word> rememberReview(@Param("userId") int userId, @Param("limit") int limit);

	@Select("select count(*) from word")
	int count();

	@Select("select count(*) from word_remember where (wrong + correct) >= 3 and correct / (wrong + correct) >= 0.85")
	int countRemember();

	@Select("select * from word where id = #{id}")
	Word get(@Param("id") Integer id);


}
