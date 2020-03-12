package me.kazechin.janword.grammar;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SentenceDao {

	@Insert("insert into sentence(sentence, grammar_id) values(#{sentence}, #{grammarId})")
	void add(@Param("sentence") String sentence, @Param("grammarId") int grammarId);

	@Select("select id,sentence from sentence where grammar_id = #{grammarId}")
	List<Sentence> list(@Param("grammarId") int grammarId);

	@Update("update sentence set sentence = #{sentence} where id = #{id}")
	void update(Sentence sentence);

	@Delete("delete from sentence where grammar_id = #{grammar_id}")
	void remove(int grammarId);


}
