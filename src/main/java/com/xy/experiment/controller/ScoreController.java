package com.xy.experiment.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.xy.experiment.config.UploadConfig;
import com.xy.experiment.entity.ExperimentScore;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.entity.VirtualScore;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.facade.CheckUser;
import com.xy.experiment.mapper.ExperimentScoreMapper;
import com.xy.experiment.mapper.ExperimentUserMapper;
import com.xy.experiment.mapper.VirtualScoreMapper;
import com.xy.experiment.utils.MD5;
import com.xy.experiment.vo.ScoreVo;
import com.xy.experiment.vo.VirtualScoreVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/exp")
public class ScoreController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private ExperimentScoreMapper scoreMapper;
    @Autowired
    private ExperimentUserMapper userMapper;
    @Autowired
    private VirtualScoreMapper virtualMapper;
    @Autowired
    private CheckUser checkUser;
    @Autowired
    private UploadConfig uploadConfig;
    @Value("${virtual.save.urlprefix}")
    private String urlprefix;

    /**
     * 新增分数
     *
     * @param scoreVo
     * @param request
     * @param response
     */
    @RequestMapping(value = "/insertscore", method = RequestMethod.POST)
    void insertScore(@RequestBody ScoreVo scoreVo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增学生实验分数数据：{}", JSONObject.toJSONString(scoreVo));
        try {
            if (StringUtil.isEmpty(scoreVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生学号不能为空!");
            }
            if (scoreVo.getScore() < 0 || scoreVo.getScore() > 100) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验成绩不能小于0分，并且不能大于100分!");
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(scoreVo.getExpStart(), dtf);
            LocalDateTime endDate = LocalDateTime.parse(scoreVo.getExpEnd(), dtf);
            if (startDate.isAfter(endDate)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验开始时间不能晚于结束时间!");
            }
            if (scoreVo.getExpTime() <= 0 || scoreVo.getExpTime() > 600) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验时长不能小于等于0分钟，并且大于10小时!");
            }
            ExperimentScore score = new ExperimentScore();
            BeanUtils.copyProperties(scoreVo, score);
            int resNum = scoreMapper.insert(score);
            if (resNum != 1) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "新增学生实验记录失败!");
            }
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增学生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增学生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增学生实验记录失败，系统错误!");
        } finally {
            logger.info("新增学生实验记录结束!");
        }
    }

    /**
     * 查询学生分数
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/queryscore", method = RequestMethod.POST)
    void queryScore(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询学生实验分数数据：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String accessToken = req.getString("accessToken");
            String account = req.getString("account");
            Integer pageNum = req.getInteger("pageNum");
            Integer pageSize = req.getInteger("pageSize");
            if (StringUtil.isEmpty(account)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生学号不能为空!");
            }
            if (pageNum <= 0 || pageNum > 10000000) {
                pageNum = 1;
            }
            if (pageSize <= 0 || pageSize > 1000) {
                pageSize = 10;
            }
            String checkToken = MD5.MD5Encode(account + pageNum + pageSize);
            if (!checkToken.equals(accessToken)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "验证参数错误，请重新查询!");
            }
            ExperimentUser user = userMapper.getOne(account);
            if (null == user) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "实验学生不存在，请核实后再查询!");
            }
            List<ExperimentScore> list;
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
            if ("admin".contains(account)) {
                list = scoreMapper.getAll();
            } else {
                list = scoreMapper.getOne(account);
            }
            JSONObject result = new JSONObject();
            result.put("list", JSONObject.toJSONString(list));
            result.put("pageNum", objects.getPageNum());
            result.put("pageSize", objects.getPageSize());
            result.put("total", objects.getTotal());
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("查询学生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("查询学生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "查询学生实验记录失败，系统错误!");
        } finally {
            logger.info("查询学生实验记录结束!");
        }
    }

    /**
     * 验证用户token
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/checkuser", method = RequestMethod.POST)
    void checkUser(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("验证用户登陆有效性开始：{}", reqJsonStr);
        try {
//            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String token = req.getString("token");
            if (StringUtil.isEmpty(token)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "验证token不能为空!");
            }
            String checkResult = checkUser.checkToken(token);
            if (StringUtil.isEmpty(checkResult)) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "验证失败!");
            }
            JSONObject result = new JSONObject();
            result.put("result", checkResult);
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("验证登陆token失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("验证登陆token失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "验证登陆token失败，系统错误!");
        } finally {
            logger.info("验证登陆token结束!");
        }
    }

    /**
     * 登陆虚拟实验平台
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/loginvirtual", method = RequestMethod.POST)
    void loginVirtual(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户登陆开始：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String username = req.getString("username");
            String password = req.getString("password");
            if (StringUtil.isEmpty(username)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户名称不能为空!");
            }
            if (StringUtil.isEmpty(password)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户密码不能为空!");
            }
            String checkResult = checkUser.loginVirtual(username, password);
            if (StringUtil.isEmpty(checkResult)) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "登陆失败!");
            }
            JSONObject result = new JSONObject();
            result.put("result", checkResult);
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("用户登陆失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("用户登陆失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "用户登陆失败，系统错误!");
        } finally {
            logger.info("用户登陆结束!");
        }
    }

    /**
     * 分数上传
     *
     * @param scoreVo
     * @param request
     * @param response
     */
    @RequestMapping(value = "/uploadscore", method = RequestMethod.POST)
    void uploadScore(@RequestBody VirtualScoreVo scoreVo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增虚拟实验分数数据：{}", JSONObject.toJSONString(scoreVo));
        try {
            if (scoreVo.getScore() < 0 || scoreVo.getScore() > 100) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验成绩不能小于0分，并且不能大于100分!");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
            Long timeLong = Long.parseLong(scoreVo.getStartDate());
            Date startDate = sdf.parse(sdf.format(timeLong));
            timeLong = Long.parseLong(scoreVo.getEndDate());
            Date endDate = sdf.parse(sdf.format(timeLong));
            if (startDate.after(endDate)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验开始时间不能晚于结束时间!");
            }
            if (scoreVo.getTimeUsed() <= 0 || scoreVo.getTimeUsed() > 600) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验时长不能小于等于0分钟，并且大于10小时!");
            }
            VirtualScore score = new VirtualScore();
            BeanUtils.copyProperties(scoreVo, score);
            int resNum = virtualMapper.insert(score);
            if (resNum != 1) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "新增虚拟实验记录失败!");
            }
//            String resultStr = checkUser.uploadScore(scoreVo);
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增学生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增学生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增学生实验记录失败，系统错误!");
        } finally {
            logger.info("新增学生实验记录结束!");
        }
    }

    /**
     * 上传文件
     *
     * @param files
     * @param request
     * @param response
     */
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    void uploadFile(@RequestParam("file") MultipartFile[] files,
                    @RequestParam("username") String username,
                    HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户上传文件开始......");
        try {
            if (null == files || files.length <= 0) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "文件不能为空！");
            }
            if (StringUtil.isEmpty(username)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户名称不能为空！");
            }
            int i = 0;
            String fileName = null;
            for (MultipartFile file : files) {
                if (file == null) continue;
                fileName = username + "-" + LocalDate.now() + "-" + file.getOriginalFilename();
                File dest = new File(urlprefix + fileName);
                try {
                    file.transferTo(dest);
                    logger.info("第" + (i++ + 1) + "个文件上传成功");
                } catch (IOException e) {
                    logger.error("文件上传异常!", e);
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "文件上传异常!");
                }
                break;
            }
            String resultStr = checkUser.uploadFile(urlprefix, fileName);
            if (StringUtil.isEmpty(resultStr)) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "文件上传失败!");
            }
            JSONObject result = new JSONObject();
            result.put("result", resultStr);
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("用户上传文件失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("用户上传文件失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "用户上传文件失败，系统错误!");
        } finally {
            logger.info("用户上传文件结束!");
        }
    }

    /**
     * 检查用户状态
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/checkstatus", method = RequestMethod.POST)
    void checkStatus(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("检查用户状态开始：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String username = req.getString("username");
            if (StringUtil.isEmpty(username)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "用户名称不能为空!");
            }
            String checkResult = checkUser.checkStatus(username);
            if (StringUtil.isEmpty(checkResult)) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户状态检查失败!");
            }
            JSONObject result = new JSONObject();
            result.put("result", checkResult);
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("用户上传文件失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("用户状态检查失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "用户状态检查失败，系统错误!");
        } finally {
            logger.info("用户状态检查结束!");
        }
    }

}
