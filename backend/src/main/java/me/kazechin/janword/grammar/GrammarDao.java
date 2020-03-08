package me.kazechin.janword.grammar;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GrammarDao {

	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
	@Insert("insert into grammar (grammar, detail) values(#{grammar}, #{detail})")
	void add(Grammar grammar);

	@Select("select id, grammar, detail from grammar")
	List<Grammar> list();

	@Delete("delete from grammar where id = #{id}")
	void remove(@Param("id") int id);
}
