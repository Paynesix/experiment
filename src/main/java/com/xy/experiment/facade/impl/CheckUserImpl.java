package com.xy.experiment.facade.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.facade.CheckUser;
import com.xy.experiment.utils.HMACSHA256;
import com.xy.experiment.utils.HttpUtils;
import com.xy.experiment.utils.RandomStringGenerator;
import com.xy.experiment.utils.jwt.JwtUtil;
import com.xy.experiment.vo.VirtualScoreVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Configuration
public class CheckUserImpl implements CheckUser {

    private static String serverURI = "http://202.205.145.156:8017";

    private static String loginId = "100400";

    private final static Logger logger = LoggerFactory.getLogger(CheckUserImpl.class);

    @Override
    public String checkToken(String token) {
        logger.info("验证用户token正确性:{}", token);
        try {
            return JwtUtil.dencrty(token);
        } catch (UnsupportedEncodingException e) {
            logger.error("验证token异常,不支持的编码类型!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证token异常,不支持的编码类型!");
        } catch (ExperimentException e) {
            logger.error("验证token失败!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证token失败!");
        } catch (Exception e) {
            logger.error("验证token异常,系统异常!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证token异常,系统异常!");
        }
    }

    @Override
    public String loginVirtual(String username, String password) {
        logger.info("用户登陆虚拟平台账号:{},密码:{}", username, password);
        try {
            String cnonce = RandomStringGenerator.getRandomStringByLength(16);
            String nonce = RandomStringGenerator.getRandomStringByLength(16);
            password = HMACSHA256
                    .SHA256(nonce + HMACSHA256.SHA256(password).toUpperCase() + cnonce)
                    .toUpperCase();
            String url = serverURI + "/sys/api/user/validate?";
            String postUrl = url + "username=" + username
                    + "&password=" + password
                    + "&nonce=" + nonce
                    + "&cnonce=" + cnonce;
            try {
                JSONObject sendJsn = new JSONObject();
                String sr = HttpUtils.sendPost(postUrl, sendJsn);
                return sr;
            } catch (Exception e) {
                logger.error("登陆虚拟平台失败!", e);
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "登陆虚拟平台失败!");
            }
        } catch (Exception e) {
            logger.error("登陆虚拟平台失败,系统异常!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "登陆虚拟平台失败,系统异常!");
        }
    }

    @Override
    public String uploadScore(VirtualScoreVo vo) {
        if(logger.isDebugEnabled()) logger.debug("上传文件系统参数：{}", JSONObject.toJSONString(vo));
        try {
            JSONObject param = new JSONObject();
            if (StringUtil.isEmpty(vo.getIssuerId())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户名不能为空!");
            }
            param.put("username", new String(vo.getUsername().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            if (StringUtil.isEmpty(vo.getProjectTitle())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验名称不能为空!");
            }
            String projectTitle = new String(vo.getProjectTitle().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            param.put("projectTitle", projectTitle);

            String childProjectTitle = StringUtil.isEmpty(vo.getChildProjectTitle()) ? "" : vo.getChildProjectTitle();
            childProjectTitle = new String(childProjectTitle.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            param.put("childProjectTitle", childProjectTitle);

            param.put("status", vo.getStatus());
            param.put("score", vo.getScore());
            param.put("startDate", vo.getStartDate());
            param.put("endDate", vo.getEndDate());
            param.put("timeUsed", vo.getTimeUsed());
            param.put("issuerId", vo.getIssuerId());
            param.put("attachmentId", vo.getAttachmentId());
            logger.info("上传文件系统参数:{}", param.toJSONString());
            String xjwt = JwtUtil.encrty(param.toJSONString());
            String url = serverURI + "/project/log/upload?xjwt=" + xjwt;
            String sr = HttpUtils.sendPost(url, new JSONObject());
            return sr;
        } catch (ExperimentException e) {
            logger.error("上传虚拟实验平台实验分数失败!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "上传虚拟实验平台实验分数失败!");
        } catch (Exception e) {
            logger.error("上传虚拟实验平台实验分数失败!系统异常", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "上传虚拟实验平台实验分数失败!系统异常");
        }
    }

    @Override
    public String uploadFile(String filePath, String fileName) {
        logger.info("虚拟实验平台上传文件路径：{}, 文件名称：{}", filePath, fileName);
        try {
            if (StringUtil.isEmpty(filePath)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "文件路径不能为空!");
            }
            if (StringUtil.isEmpty(fileName)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "文件名称不能为空!");
            }
            return HttpUtils.upload(filePath, fileName);
        } catch (Exception e) {
            logger.error("文件上传异常,系统异常!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "文件上传异常,系统异常!");
        }
    }

    @Override
    public String checkStatus(String username) {
        logger.info("验证用户登陆状态,用户名称：{}", username);
        try {
            if(StringUtil.isEmpty(username)){
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户名称不能为空!");
            }
            JSONObject param = new JSONObject();
            param.put("username", username);
            param.put("issuerId", loginId);
            logger.info("==============>验证用户登陆状态：{} ", param.toJSONString());
            String xjwt = JwtUtil.encrty(param.toJSONString());
            String url = serverURI + "/third/api/test/result/upload?xjwt=" + xjwt;
            String sr = HttpUtils.sendPost(url, new JSONObject());
            return sr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ExperimentException e) {
            logger.error("验证用户登陆状态失败!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证用户登陆状态失败!");
        } catch (Exception e) {
            logger.error("验证用户登陆状态失败,系统异常!", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证用户登陆状态失败,系统异常!");
        }
        return null;
    }


}
