package me.kazechin.janword.word;

import me.kazechin.janword.word.Word;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WordDao {

	@Select("select * from word;")
	List<Word> list();

	@Insert("insert into word (word, gana, chinese) values(#{word}, #{gana}, #{chinese})")
	void add(Word word);

	@Delete("delete from word where id = #{id}")
	void remove(int id);

	@Update("update word set word=#{word}, gana=#{gana}, chinese=#{chinese} where id=#{id}")
	void modify(Word word);

	@Select("select word.id, word.word, word.gana, word.chinese from word left join word_remember " +
			"on word_remember.word_id = word.id " +
			"and (" +
			"word_remember.last_date is null or " +
			"word_remember.last_date < date_sub(now(), INTERVAL 7 DAY) " +
			")")
	List<Word> remember();

	@Select("select * from word where id = #{id}")
	Word get(@Param("id") Integer id);

}
