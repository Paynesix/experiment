package com.xy.experiment.controller;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.cache.ExEnum;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.mapper.ExperimentUserMapper;
import com.xy.experiment.utils.RandomStringGenerator;
import com.xy.experiment.vo.EmailCacheVo;
import com.xy.experiment.vo.UserVo;
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
import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserControllerTests {

    private final static Logger logger = LoggerFactory.getLogger(UserControllerTests.class);

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ExperimentUserMapper userMapper;

    protected MockMvc mockMvc;

    @Test
    void registerUser() {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String account = localDateTime.format(dtf);

            UserVo userVo = new UserVo();
            userVo.setAccount(account);
            userVo.setEmail(account + "@163.com");
            userVo.setPassword(RandomStringGenerator.getRandomPassword(8));
            String jsonStr = JSONObject.toJSONString(userVo);
            logger.info("=========> 注册用户开始：{}", jsonStr);

            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
            ResultActions actions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/exp/registeruser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonStr)//请求体时json
            );
            actions.andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = actions.andReturn();
            JSONObject res = (JSONObject) JSONObject.parse(mvcResult.getResponse().getContentAsString());
            logger.info("========> 注册账号结束：{}", res.toJSONString());
            assert res.getString("code").equals("0");
        } catch (Exception e) {
            logger.error("注册账号失败!", e);
        }
    }

    @Test
    void loginUser() {
        try {
            List<ExperimentUser> user = userMapper.getAll();
            if (null == user) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "数据库账号为空!");
            }
            logger.info("========> 登陆账号开始：{}", JSONObject.toJSONString(user.get(0)));
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
            ResultActions actions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/exp/loginuser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONObject.toJSONString(user.get(0)))
            );
            actions.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject res = (JSONObject) JSONObject.parse(
                    actions.andReturn().getResponse().getContentAsString());
            logger.info("=============> 登陆结束：{}", res.toJSONString());
        } catch (Exception e) {
            logger.error("登陆失败!", e);
        }
    }

    @Test
    void sendEmail() {
        try {
            List<ExperimentUser> user = userMapper.getAll();
            if (null == user) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "数据库账号为空!");
            }
            JSONObject params = new JSONObject();
            params.put("email", user.get(0).getEmail());
            logger.info("========> 发送邮箱开始：{}", JSONObject.toJSONString(user.get(0)));
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
            ResultActions actions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/exp/sendemail")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(params.toJSONString())
            );
            actions.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject res = (JSONObject) JSONObject.parse(
                    actions.andReturn().getResponse().getContentAsString());
            logger.info("=============> 发送邮箱结束：{}", res.toJSONString());
            assert res.getString("code").equals("0")| res.getString("code").equals("110003")
                    |res.getString("code").equals("110002")|res.getString("code").equals("110001");
        } catch (Exception e) {
            logger.error("发送邮箱失败!");
        }
    }

    @Test
    void updatePassword() {
        try {
            List<ExperimentUser> users = userMapper.getAll();
            if (null == users) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "数据库账号为空!");
            }
            ExperimentUser user = users.get(0);
            user.setPassword(URLEncoder.encode(RandomStringGenerator.getRandomPassword(8), "UTF-8"));
            String token = RandomStringGenerator.getRandomStringByLength(4);
            Map<String, Object> emailCache = ExEnum.getInstance().getEmailCache();
            EmailCacheVo cacheVo = new EmailCacheVo();
            cacheVo.setEmail(user.getEmail());
            cacheVo.setToken(token);
            cacheVo.setSendEmailDate(LocalDateTime.now());
            emailCache.put(cacheVo.getEmail(), cacheVo);

            JSONObject params = new JSONObject();
            params.put("account", user.getAccount());
            params.put("email", user.getEmail());
            params.put("password", user.getPassword());
            params.put("token", token);

            logger.info("========> 更新密码开始：{}", params.toJSONString());
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
            ResultActions actions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/exp/updatepassword")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(params.toJSONString())
            );
            actions.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject res = (JSONObject) JSONObject.parse(
                    actions.andReturn().getResponse().getContentAsString());
            logger.info("=============> 更新密码结束：{}", res.toJSONString());
            assert res.getString("code").equals("0");
        } catch (Exception e) {
            logger.error("更新密码错误!");
        }
    }

}
