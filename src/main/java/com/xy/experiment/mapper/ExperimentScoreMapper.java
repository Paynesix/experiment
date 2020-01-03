package com.xy.experiment.mapper;


import com.xy.experiment.entity.ExperimentScore;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ExperimentScoreMapper {


    @Select("SELECT * FROM experiment_score")
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
    List<ExperimentScore> getAll();

    @Select("SELECT * FROM experiment_score WHERE account = #{account}")
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
    List<ExperimentScore> getOne(String account);

    @Select("SELECT * FROM experiment_score WHERE account = #{account}" +
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
    ExperimentScore getOneScore(ExperimentScore score);

    @Insert("INSERT INTO experiment_score(account, school_name, exp_start, exp_end, exp_time, score, is_qualified) " +
            "values (#{account},#{schoolName},#{expStart},#{expEnd},#{expTime},#{score},#{isQualified})")
    int insert(ExperimentScore score);

    @Update("UPDATE experiment_score SET school_name=#{schoolName}, score=#{score} " +
            "WHERE id =#{id}")
    int update(ExperimentScore score);

    @Delete("DELETE FROM experiment_score WHERE id =#{id}")
    void delete(Integer id);

}
