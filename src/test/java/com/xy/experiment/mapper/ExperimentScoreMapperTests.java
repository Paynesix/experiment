package com.xy.experiment.mapper;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.entity.ExperimentScore;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class ExperimentScoreMapperTests {


    private final static Logger logger = LoggerFactory.getLogger(ExperimentUserMapperTests.class);

    @Autowired
    private ExperimentScoreMapper scoreMapper;

    @Test
    void getAll() {
        logger.debug("getAll start");
        List<ExperimentScore> list = scoreMapper.getAll();
        list.stream().forEach(o-> System.out.println(JSONObject.toJSONString(o)));
        logger.debug("getAll end");
    }

    @Test
    void getOne(){
        logger.debug("getOne start");
        List<ExperimentScore> users = scoreMapper.getOne("201901010001");
        System.out.println(JSONObject.toJSONString(users));
        logger.debug("getOne end");
    }

    @Test
    void insertOne(){
        logger.debug("insert one score start");
        ExperimentScore score = new ExperimentScore();
        score.setAccount("201901010001");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = now.plusMinutes(30);
        score.setExpStart(now.format(dtf));
        score.setExpEnd(after.format(dtf));
        score.setExpTime(10);
        score.setIsQualified(0);
        int resNum = scoreMapper.insert(score);
        logger.debug("insert one score end" + resNum);
    }
}
