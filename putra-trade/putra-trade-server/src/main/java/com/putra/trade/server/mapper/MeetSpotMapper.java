package com.putra.trade.server.mapper;

import com.putra.trade.pojo.entity.MeetSpot;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MeetSpotMapper {

    @Select("SELECT * FROM meet_spot WHERE member_id = #{memberId} ORDER BY is_default DESC, update_time DESC")
    List<MeetSpot> listByMember(Long memberId);

    @Select("SELECT * FROM meet_spot WHERE id = #{id}")
    MeetSpot getById(Long id);

    @Insert("INSERT INTO meet_spot (member_id, contact_name, contact_phone, spot_name, remark, is_default, create_time, update_time) " +
            "VALUES (#{memberId}, #{contactName}, #{contactPhone}, #{spotName}, #{remark}, #{isDefault}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(MeetSpot spot);

    @Update("UPDATE meet_spot SET contact_name=#{contactName}, contact_phone=#{contactPhone}, spot_name=#{spotName}, " +
            "remark=#{remark}, is_default=#{isDefault}, update_time=NOW() WHERE id=#{id}")
    void update(MeetSpot spot);

    @Delete("DELETE FROM meet_spot WHERE id = #{id}")
    void deleteById(Long id);

    @Update("UPDATE meet_spot SET is_default = 0 WHERE member_id = #{memberId}")
    void clearDefault(Long memberId);
}
