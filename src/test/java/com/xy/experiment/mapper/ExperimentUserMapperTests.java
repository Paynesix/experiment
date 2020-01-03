package com.xy.experiment.mapper;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.utils.MD5;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class ExperimentUserMapperTests {

    private final static Logger logger = LoggerFactory.getLogger(ExperimentUserMapperTests.class);

    @Autowired
    private ExperimentUserMapper userMapper;

    @Test
    void getAll() {
        logger.debug("getAll start");
        List<ExperimentUser> list = userMapper.getAll();
        list.stream().forEach(o -> System.out.println(JSONObject.toJSONString(o)));
        logger.debug("getAll end");
    }

    @Test
    void getOne() {
        logger.debug("getOne start");
        ExperimentUser user = userMapper.getOne("18373200001");
        System.out.println(JSONObject.toJSONString(user));
        logger.debug("getOne end");
    }

    @Test
    void insertUser() throws Exception {
        try {
            logger.debug("insert user start");
            ExperimentUser user = new ExperimentUser();
            user.setAccount("201901010001");
            String password = "123456";
            String mdPassword;
            try {
                mdPassword = MD5.MD5Encode(password + user.getAccount());
            } catch (Exception e) {
                logger.error("密码加密失败!");
                return;
            }
            user.setPassword(password);
            user.setPhone("13187030387");
            String email = "paynesix@163.com";
            String check = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                user.setEmail(email);
            } else {
                throw new Exception("邮箱格式不正确!");
            }
            ExperimentUser exUser = userMapper.getOne(user.getAccount());
            if (exUser != null) {
                throw new Exception("用户已经存在，请重新注册，或者重置密码");
            }
            user.setName("xy");
            int resNum = userMapper.insert(user);
            logger.debug("insert user end" + resNum);
        } catch (Exception e) {
            logger.error("增加用户测试用例失败!", e);
        }
    }

    @Test
    void updateUser() throws Exception {
        try {
            logger.debug("更新用户密码 start");
            ExperimentUser user = new ExperimentUser();
            user.setAccount("201901010001");
            String password = "123qwe";
            String mdPassword;
            try {
                mdPassword = MD5.MD5Encode(password + user.getAccount());
            } catch (Exception e) {
                logger.error("密码加密失败!");
                throw new Exception("加密失败");
            }
            user.setPassword(password);
            user.setPhone("13187030387");
            ExperimentUser exUser = userMapper.getOne(user.getAccount());
            if (exUser == null) {
                throw new Exception("用户不存在，请先注册");
            }
            int resNum = userMapper.update(user);
            logger.debug("更新用户密码 end " + resNum);
        } catch (Exception e) {
            logger.error("更新用户密码失败!");
        }
    }
}
