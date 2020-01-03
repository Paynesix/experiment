package com.xy.experiment.controller;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.mapper.ExperimentUserMapper;
import com.xy.experiment.utils.MD5;
import com.xy.experiment.vo.ScoreVo;
import com.xy.experiment.vo.VirtualScoreVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ScoreControllerTests {

    private final static Logger logger = LoggerFactory.getLogger(ScoreControllerTests.class);

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ExperimentUserMapper userMapper;

    @Test
    public void mockInsertScoreTest() {
        try {
            logger.info("=======> 新增学生分数记录测试开始");
            List<ExperimentUser> userVos = userMapper.getAll();
            if (userVos == null || userVos.size() == 0) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "数据库账号为空!");
            }
            ExperimentUser user = userVos.get(0);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.now();
            int expTime = Math.abs((new Random()).nextInt()) % 50 + 10;
            LocalDateTime endDate = startDate.plusMinutes(expTime);
            int score = Math.abs((new Random()).nextInt() + 50) % 100;

            ScoreVo scoreVo = new ScoreVo();
            scoreVo.setAccount(user.getAccount());
            scoreVo.setSchoolName(user.getSchoolName());
            scoreVo.setExpStart(startDate.format(dtf));
            scoreVo.setExpEnd(endDate.format(dtf));
            scoreVo.setExpTime(expTime);
            scoreVo.setScore(score);
            scoreVo.setIsQualified(score >= 60 ? 0 : 1);
            scoreVo.setMemo(user.getAccount() + " score test score: " + score);
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/insertscore")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(JSONObject.toJSONString(scoreVo))
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 新增学生分数记录测试结束：{}", res.toJSONString());
            assert res.getString("code").equals("0");
        } catch (Exception e) {
            logger.error("更新密码错误!");
        }
    }

    @Test
    public void mockQueryScoreTest() {
        try {
            logger.info("=======> 查询学生分数记录测试开始：admin");
            String account = "admin";
            String accessToken = MD5.MD5Encode(account + "1" + "10");
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

            JSONObject params = new JSONObject();
            params.put("account", account);
            params.put("pageNum", 1);
            params.put("pageSize", 10);
            params.put("accessToken", accessToken);

            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/queryscore")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(params.toJSONString())
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 新增学生分数记录测试结束 admin ：{}", res.toJSONString());
            String code = res.getString("code");
            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
        } catch (Exception e) {
            logger.error("更新密码错误!");
        }
    }

    @Test
    public void mockCheckUserTest() {
        try {
            logger.info("=======> 检查用户登陆token测试开始：admin");
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

            String token = "AAABbwgvVgkBAAAAAAABiBk%3D.8cQMyMPS7%2F0Ghx6SvQ9GwHQ9gwPpRmtMytnx4kWSZozVeBRHp1qV7ogq1%2BHQEJLKJiKj55jIdoWm0MQScPSvF%2BpZWXs2TRf9xnnHP%2Bm855AOCzY9ri4psVvfMgRGpVnbCcyCJAIpUUitEiVCJyXzQsXEP4JR3%2FWH0xYidQ2z5Sx3KjyUPR2iBpeDqD9UXHbHdmU3jDdieFo9MVX9aokP9fsZuCZ94Ri2ClgCcosS9CtCjW6dByZd0chMsPLXGtJi8Tu7BW08XnEH5OiFQSkt%2Bzz9nIDfGkPHo2%2B71ze2qR0%3D.TcKopVS4tqB63kK7QFm3ZqK8yM8vnehiCI3Nk4yEqTE%3D";
            JSONObject params = new JSONObject();
            params.put("token", URLEncoder.encode(token, "utf-8"));

            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/checkuser")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(params.toJSONString())
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 检查用户登陆token测试结束 admin ：{}", res.toJSONString());
            String code = res.getString("code");
            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
        } catch (Exception e) {
            logger.error("检查用户登陆token错误!");
        }
    }

    @Test
    public void mockLoginVirtualTest() {
        try {
            logger.info("=======> 用户登陆虚拟账号测试开始.");
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

            JSONObject params = new JSONObject();
            params.put("username", "test");
            params.put("password", "123456");

            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/loginvirtual")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(params.toJSONString())
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 用户登陆虚拟账号测试结束 admin ：{}", res.toJSONString());
            String code = res.getString("code");
            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
        } catch (Exception e) {
            logger.error("用户登陆虚拟账号错误!");
        }
    }

    @Test
    public void mockUploadScoreTest() {
        try {
            logger.info("=======> 用户上传实验分数测试开始.");
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

            int score = Math.abs((new Random()).nextInt() + 50) % 100;
            int expTime = Math.abs((new Random()).nextInt() + 50) % 100;
            Calendar rightNow = Calendar.getInstance();

            VirtualScoreVo scoreVo = new VirtualScoreVo();
            scoreVo.setAccount("123");
            scoreVo.setIssuerId("100327");
//            scoreVo.setAttachmentId(1);
            scoreVo.setProjectTitle("实验室安全VR教学系统");
            scoreVo.setChildProjectTitle("");
            scoreVo.setEndDate(String.valueOf(rightNow.getTimeInMillis()));
            rightNow.add(Calendar.MINUTE, -expTime);
            scoreVo.setStartDate(String.valueOf(rightNow.getTimeInMillis()));
            scoreVo.setTimeUsed(expTime);
            scoreVo.setScore(score);
            scoreVo.setStatus(score>60?1:2);
            scoreVo.setUsername("test");
            scoreVo.setDemo("");

            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/uploadscore")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(JSONObject.toJSONString(scoreVo))
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 用户上传实验分数测试结束 admin ：{}", res.toJSONString());
            String code = res.getString("code");
            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
        } catch (Exception e) {
            logger.error("用户上传实验分数错误!");
        }
    }

    @Test
    public void mockUploadFileTest() {
//        try {
//            logger.info("=======> 用户上传实验分数测试开始.");
//            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
//            JSONObject param = new JSONObject();
//            param.put("username", "test");
//            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/exp/uploadfile")
//                    .file(new MockMultipartFile(
//                            "file",
//                            "test",
//                            "application/ms-excel",
//                            new FileInputStream(
//                                    new File("E:\\zxh\\file\\study.docx"))))
//                    .content(JSONObject.toJSONString(param))
//            );
//            reaction.andExpect(MockMvcResultMatchers.status().isOk());
//            MvcResult mvcResult = reaction.andReturn();
//            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
//            logger.info("========> 用户上传实验分数测试结束 admin ：{}", res.toJSONString());
//            String code = res.getString("code");
//            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
//        } catch (Exception e) {
//            logger.error("用户上传实验分数错误!");
//        }
    }

    @Test
    public void mockCheckStatusTest() {
        try {
            logger.info("=======> 用户上传实验分数测试开始.");
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

            JSONObject params = new JSONObject();
            params.put("username", "test");
            ResultActions reaction = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/exp/checkstatus")
                    .contentType(MediaType.APPLICATION_JSON)//请求体时json
                    .content(params.toJSONString())
            );
            reaction.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = reaction.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 用户上传实验分数测试结束 admin ：{}", res.toJSONString());
            String code = res.getString("code");
            assert "0".equals(code) | "110003".equals(code) | "110002".equals(code) | "110001".equals(code);
        } catch (Exception e) {
            logger.error("用户上传实验分数错误!");
        }
    }


}
