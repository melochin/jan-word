package me.kazechin.janword.dao;

import me.kazechin.janword.model.Sentence;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SentenceDao {

	@Insert("insert into sentence(sentence, object_id, type_no) values(#{sentence}, #{object_id}, #{type})")
	void add(@Param("sentence") String sentence, @Param("object_id") int objectId, @Param("type") int type);

	@Select("select id,sentence from sentence where object_id = #{object_id} and type_no = #{type}")
	List<Sentence> list(@Param("object_id") int objectId, @Param("type") int type);

	@Update("update sentence set sentence = #{sentence} where id = #{id}")
	void update(Sentence sentence);

	@Delete("delete from sentence where object_id = #{object_id} and type_no = #{type}")
	void remove(@Param("object_id") int object_id, @Param("type") int type);

}
