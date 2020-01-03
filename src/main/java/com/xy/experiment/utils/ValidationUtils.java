package com.xy.experiment.utils;

import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private final static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    public static boolean validaEmail(String email){
        logger.info("邮箱验证开始：{}", email);
        boolean flag = false;
        if(StringUtil.isEmpty(email)){
            logger.info("邮箱验证结束：{}", false);
            return false;
        }
        String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        if(isMatched){
            flag = true;
        }else{
            flag = false;
        }
        logger.info("邮箱验证结束:{}", flag);
        return flag;
    }

    public static boolean validaPassword(String password){
        logger.info("密码验证开始：{}", password);
        boolean flag = false;
        if(StringUtil.isEmpty(password)){
            logger.info("邮箱验证结束：{}", false);
            return false;
        }
        String check = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(password);
        boolean isMatched = matcher.matches();
        if(isMatched){
            flag = true;
        }else{
            flag = false;
        }
        logger.info("密码验证结束:{}", flag);
        return flag;
    }
}
