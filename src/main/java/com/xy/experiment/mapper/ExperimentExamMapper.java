package com.xy.experiment.mapper;


import com.xy.experiment.entity.ExperimentExam;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ExperimentExamMapper {


    @Select("SELECT * FROM experiment_exam")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "account", column = "account"),
        @Result(property = "type", column = "type"),
        @Result(property = "startDate", column = "start_date"),
        @Result(property = "stopDate", column = "stop_date"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "hintNum", column = "hint_num"),
        @Result(property = "mistakeNum", column = "mistake_num"),
        @Result(property = "score", column = "score"),
        @Result(property = "memo", column = "memo"),
        @Result(property = "version", column = "version"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<ExperimentExam> getAll();

    @Select("SELECT * FROM experiment_exam where type = #{type}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "account", column = "account"),
        @Result(property = "type", column = "type"),
        @Result(property = "startDate", column = "start_date"),
        @Result(property = "stopDate", column = "stop_date"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "hintNum", column = "hint_num"),
        @Result(property = "mistakeNum", column = "mistake_num"),
        @Result(property = "score", column = "score"),
        @Result(property = "memo", column = "memo"),
        @Result(property = "version", column = "version"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<ExperimentExam> getAllByType(String type);

    @Select("SELECT * FROM experiment_exam WHERE account = #{account}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "type", column = "type"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "stopDate", column = "stop_date"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "hintNum", column = "hint_num"),
            @Result(property = "mistakeNum", column = "mistake_num"),
            @Result(property = "score", column = "score")
    })
    ExperimentExam getOneScore(String account);

    @Select("SELECT * FROM experiment_exam WHERE account = #{account} and type = #{type}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "type", column = "type"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "stopDate", column = "stop_date"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "hintNum", column = "hint_num"),
            @Result(property = "mistakeNum", column = "mistake_num"),
            @Result(property = "score", column = "score")
    })
    ExperimentExam getOneScoreByAccAndType(String account, String type);

    @Select("SELECT * FROM experiment_exam WHERE account = #{account}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "type", column = "type"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "stopDate", column = "stop_date"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "hintNum", column = "hint_num"),
            @Result(property = "mistakeNum", column = "mistake_num"),
            @Result(property = "score", column = "score")
    })
    List<ExperimentExam> getList(String account);

    @Select("SELECT * FROM experiment_exam WHERE account = #{account} and type = #{type}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "type", column = "type"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "stopDate", column = "stop_date"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "hintNum", column = "hint_num"),
            @Result(property = "mistakeNum", column = "mistake_num"),
            @Result(property = "score", column = "score")
    })
    List<ExperimentExam> getOneByAccountAndType(String account, String type);

    @Insert("INSERT INTO experiment_exam(account, type, start_date, stop_date, duration, hint_num, mistake_num, score, memo) " +
            "values (#{account},#{type},#{startDate},#{stopDate},#{duration},#{hintNum},#{mistakeNum},#{score},#{memo})")
    int insert(ExperimentExam score);

    @Update("UPDATE experiment_exam SET score=#{score}, memo=#{memo}, start_date=#{startDate}," +
            " stop_date=#{stopDate}, duration=#{duration}, hint_num=#{hintNum}, mistake_num=#{mistakeNum} " +
            "WHERE account =#{account} and type =#{type}")
    int update(ExperimentExam score);

    @Delete("DELETE FROM experiment_exam WHERE id =#{id}")
    void delete(Long id);

    @Select("select count(distinct account) from experiment_exam")
    long queryUserCount();

}
