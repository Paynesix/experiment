package com.xy.experiment.mapper;


import com.xy.experiment.entity.VirtualScore;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface VirtualScoreMapper {


    @Select("SELECT * FROM virtual_score")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "account", column = "account"),
        @Result(property = "schoolName", column = "school_name"),
        @Result(property = "expStart", column = "exp_start"),
        @Result(property = "expEnd", column = "exp_end"),
        @Result(property = "expTime", column = "exp_time"),
        @Result(property = "score", column = "score"),
        @Result(property = "isQualified", column = "is_qualified")
    })
    List<VirtualScore> getAll();

    @Select("SELECT * FROM virtual_score WHERE account = #{account}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "schoolName", column = "school_name"),
            @Result(property = "expStart", column = "exp_start"),
            @Result(property = "expEnd", column = "exp_end"),
            @Result(property = "expTime", column = "exp_time"),
            @Result(property = "score", column = "score"),
            @Result(property = "isQualified", column = "is_qualified")
    })
    List<VirtualScore> getOne(String account);

    @Select("SELECT * FROM virtual_score WHERE account = #{account}" +
            " and exp_start = #{expStart} " +
            " and exp_end = #{expEnd} " +
            " and exp_time = #{expTime} " +
            " and score = #{score} " +
            " and is_qualified = #{isQualified} limit 1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "schoolName", column = "school_name"),
            @Result(property = "expStart", column = "exp_start"),
            @Result(property = "expEnd", column = "exp_end"),
            @Result(property = "expTime", column = "exp_time"),
            @Result(property = "score", column = "score"),
            @Result(property = "isQualified", column = "is_qualified")
    })
    VirtualScore getOneScore(VirtualScore score);

    @Insert("INSERT INTO experiment.virtual_score(user_name, project_title, child_project_title, status, score, start_date, end_date, time_used, issuer_id, attachment_id, account) " +
            "values (#{userName},#{projectTitle},#{childProjectTitle},#{status},#{score},#{startDate},#{endDate},#{timeUsed},#{issuerId},#{attachmentId},#{account})")
    int insert(VirtualScore score);

    @Update("UPDATE experiment.virtual_score " +
            "SET user_name = #{userName}, " +
            "project_title = #{projectTitle}, " +
            "child_project_title = #{childProjectTitle}, " +
            "status = #{status}, score = #{score}, " +
            "start_date = #{startDate}, end_date = #{endDate}, " +
            "time_used = #{timeUsed}, issuer_id = #{issuerId}, attachment_id = #{attachmentId}, " +
            "account = #{account}, memo = #{memo} " +
            "WHERE id = #{id};")
    int update(VirtualScore score);

    @Delete("DELETE FROM virtual_score WHERE id =#{id}")
    void delete(Integer id);


}
