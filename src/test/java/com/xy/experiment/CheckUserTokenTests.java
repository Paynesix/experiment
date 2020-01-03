package com.xy.experiment;

import com.xy.experiment.facade.CheckUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CheckUserTokenTests {

    @Autowired
    private CheckUser checkUser;

    private final static Logger logger = LoggerFactory.getLogger(CheckUserTokenTests.class);

    @Test
    void checkUserToken() {
//        try {
//            String token = "AAABZKECn4ABAAAAAAABhqM%3D.fKf3J5DN6Ym0Fo3I5CJYdzQMR0iwEz7QnQIit2Mfl6v03jpEJ%2Fr4FMRFqh5kN4yw.tqIPoyvkHe2MGOXMimE9O554Lo6AbBCQkZlsqQI4XRQ%3D";
//            logger.debug("token:{}", token);
//            String res = checkUser.checkToken(token);
//            logger.debug("result:{}", res);
//        }catch (Exception e){
//            logger.error("检查token错误:{}", e);
//        }
    }

}
