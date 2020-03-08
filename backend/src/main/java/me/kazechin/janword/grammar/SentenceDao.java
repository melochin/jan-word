package me.kazechin.janword.grammar;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SentenceDao {

	@Insert("insert into sentence(sentence, grammar_id) values(#{sentence}, #{grammarId})")
	void add(@Param("sentence") String sentence, @Param("grammarId") int grammarId);

	@Select("select id,sentence from sentence where grammar_id = #{grammarId}")
	List<Sentence> list(@Param("grammarId") int grammarId);
}
