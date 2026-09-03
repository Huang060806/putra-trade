package com.putra.trade.server.mapper;

import com.putra.trade.pojo.entity.SensitiveWord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SensitiveWordMapper {

    @Select("SELECT word FROM sensitive_word")
    List<String> listWords();

    @Select("SELECT * FROM sensitive_word ORDER BY create_time DESC")
    List<SensitiveWord> list();

    @Insert("INSERT INTO sensitive_word (word, type, create_time, update_time, create_user) " +
            "VALUES (#{word}, #{type}, NOW(), NOW(), #{createUser})")
    void insert(SensitiveWord word);

    @Delete("DELETE FROM sensitive_word WHERE id = #{id}")
    void deleteById(Long id);
}
