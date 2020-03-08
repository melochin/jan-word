package me.kazechin.janword.mapper;

import me.kazechin.janword.word.Word;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WordDao {

	@Select("select * from word;")
	List<Word> list();

	@Insert("insert into word (word, gana, chinese) values(#{word}, #{gana}, #{chinese})")
	void add(Word word);

	@Delete("delete from word where id = #{id}")
	void remove(int id);
}
