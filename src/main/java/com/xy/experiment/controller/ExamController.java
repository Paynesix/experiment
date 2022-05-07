package com.xy.experiment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.xy.experiment.cache.ExEnum;
import com.xy.experiment.constants.ExperimentConstants;
import com.xy.experiment.entity.ExperimentExam;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.mapper.ExperimentExamMapper;
import com.xy.experiment.mapper.ExperimentUserMapper;
import com.xy.experiment.utils.MD5;
import com.xy.experiment.vo.DownloadCacheVo;
import com.xy.experiment.vo.ExamVo;
import com.xy.experiment.vo.UserCacheVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/exp")
public class ExamController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExperimentExamMapper examMapper;
    @Autowired
    private ExperimentUserMapper userMapper;
    @Value("${experiment.exeDownPath}")
    private String exeDownPath;
    @Value("${experiment.vrExeName}")
    private String vrExeName;
    @Value("${experiment.peExeName}")
    private String peExeName;

    /**
     * 新增成绩
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/insertexam", method = RequestMethod.POST)
    void insertExam(@RequestBody  String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增考生实验分数数据：{}", reqJsonStr);
        try {
            // 1. 判断是否为空
            if(StringUtil.isEmpty(reqJsonStr)){
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "请求参数不能为空");
            }
            // 2. 找出序列号最大最小的
            Map<Integer, ExamVo> map = new HashMap<>();
            int min = 1000;
            int max = -1;
            int highScore = 0;
            JSONArray array = JSONArray.parseArray(reqJsonStr);
            ExperimentExam examVo = new ExperimentExam();
            for (int i=0 ; i<array.size(); i++){
                ExamVo vo = JSONObject.parseObject(array.getString(i), ExamVo.class);
                ExamVo.ActionDetail actionDetail = vo.getActionDetails().get(0);
                int tmpMin = Math.min(actionDetail.getSortId(), min);
                if(tmpMin < min){
                    min = tmpMin;
                    map.put(min, vo);
                }

                int tmpMax = Math.max(actionDetail.getSortId(), max);
                if(tmpMax > max){
                    max = tmpMax;
                    map.put(max, vo);
                }
                examVo.setDuration(examVo.getDuration() + actionDetail.getDuration());
                examVo.setHintNum(examVo.getHintNum() + actionDetail.getHintNum());
                examVo.setMistakeNum(examVo.getMistakeNum() + actionDetail.getMistakeNum());
                highScore = Math.max(vo.getScore(), highScore);
            }
            // 3. 求总分，最后保存
            examVo.setAccount(map.get(max).getaCId());
            examVo.setType(map.get(max).getType());
            examVo.setScore(highScore);
            examVo.setStartDate(map.get(min).getActionDetails().get(0).getStartDate());
            examVo.setStopDate(map.get(max).getActionDetails().get(0).getStopDate());

            if (StringUtil.isEmpty(examVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "考号不能为空!");
            }
            ExperimentUser exUser = userMapper.getOne(examVo.getAccount());
            if (null == exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户不存在，请先注册用户！");
            }
            if (examVo.getScore() < 0 || examVo.getScore() > 100) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验成绩不能小于0分，并且不能大于100分!");
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(examVo.getStartDate(), dtf);
            LocalDateTime endDate = LocalDateTime.parse(examVo.getStopDate(), dtf);
            if (startDate.isAfter(endDate)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验开始时间不能晚于结束时间!");
            }
            if (examVo.getDuration() < 0 || examVo.getDuration() > 3600) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验时长不能小于等于0分钟，并且大于10小时!");
            }

            examVo.setMemo(array.toJSONString());
            // 4. 保存最高分
            ExperimentExam one = examMapper.getOneScore(examVo.getAccount());
            int resNum;
            if(Objects.nonNull(one)){
                if(one.getScore() < examVo.getScore()){
                    resNum = examMapper.update(examVo);
                    if (resNum != 1) {
                        throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "更新考生实验记录失败!");
                    }
                } else {
                    logger.info("不是最高分，不用更新");
                }
            } else {
                resNum = examMapper.insert(examVo);
                logger.info("第一次考试成绩保存");
                if (resNum != 1) {
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "新增考生实验记录失败!");
                }
            }
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增考生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增考生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增考生实验记录失败，系统错误!");
        } finally {
            logger.info("新增考生实验记录结束!");
        }
    }

    /**
     * 新增成绩
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/insertexam2", method = RequestMethod.POST)
    void insertexam2(@RequestBody  String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增考生实验分数数据2：{}", reqJsonStr);
        try {
            // 1. 判断是否为空
            if(StringUtil.isEmpty(reqJsonStr)){
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "请求参数不能为空");
            }
            // 2. 找出序列号最大最小的
            Map<Integer, ExamVo> map = new HashMap<>();
            int min = 1000;
            int max = -1;
            int highScore = 0;
            ExperimentExam examVo = new ExperimentExam();
            ExamVo vo = JSONObject.parseObject(reqJsonStr, ExamVo.class);
            List<ExamVo.ActionDetail> actionDetails = vo.getActionDetails();
            for (ExamVo.ActionDetail tmp : actionDetails){
                int tmpMin = Math.min(tmp.getSortId(), min);
                if(tmpMin < min){
                    min = tmpMin;
                    map.put(min, vo);
                }

                int tmpMax = Math.max(tmp.getSortId(), max);
                if(tmpMax > max){
                    max = tmpMax;
                    map.put(max, vo);
                }
                examVo.setDuration(examVo.getDuration() + tmp.getDuration());
                examVo.setHintNum(examVo.getHintNum() + tmp.getHintNum());
                examVo.setMistakeNum(examVo.getMistakeNum() + tmp.getMistakeNum());
                highScore = Math.max(vo.getScore(), highScore);
            }
            // 3. 求总分，最后保存
            examVo.setAccount(map.get(max).getaCId());
            examVo.setType(map.get(max).getType());
            examVo.setScore(highScore);
            examVo.setStartDate(map.get(min).getActionDetails().get(0).getStartDate());
            examVo.setStopDate(map.get(max).getActionDetails().get(0).getStopDate());

            if (StringUtil.isEmpty(examVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "考号不能为空!");
            }
            ExperimentUser exUser = userMapper.getOne(examVo.getAccount());
            if (null == exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户不存在，请先注册用户！");
            }
            if (examVo.getScore() < 0 || examVo.getScore() > 100) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验成绩不能小于0分，并且不能大于100分!");
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(examVo.getStartDate(), dtf);
            LocalDateTime endDate = LocalDateTime.parse(examVo.getStopDate(), dtf);
            if (startDate.isAfter(endDate)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验开始时间不能晚于结束时间!");
            }
            if (examVo.getDuration() < 0 || examVo.getDuration() > 3600) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "实验时长不能小于等于0分钟，并且大于10小时!");
            }

            examVo.setMemo(JSON.toJSONString(vo));
            // 4. 保存最高分
            ExperimentExam one = examMapper.getOneScore(examVo.getAccount());
            int resNum;
            if(Objects.nonNull(one)){
                if(one.getScore() < examVo.getScore()){
                    resNum = examMapper.update(examVo);
                    if (resNum != 1) {
                        throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "更新考生实验记录失败!");
                    }
                } else {
                    logger.info("不是最高分，不用更新");
                }
            } else {
                resNum = examMapper.insert(examVo);
                logger.info("第一次考试成绩保存");
                if (resNum != 1) {
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "新增考生实验记录失败!");
                }
            }
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增考生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增考生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增考生实验记录失败，系统错误!");
        } finally {
            logger.info("新增考生实验记录结束!");
        }
    }

    /**
     * 查询考生分数
     *
     * @param reqJsonStr
     * @param request
     * @param response
     */
    @RequestMapping(value = "/queryexam", method = RequestMethod.POST)
    void queryExam(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询考生实验分数数据：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String account = req.getString("account");
            Integer pageNum = req.getInteger("pageNum");
            Integer pageSize = req.getInteger("pageSize");
            String type = req.getString("type");// 预留，后续分实验类型进行开发
            if(StringUtil.isEmpty(account)){
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "参数不能为空");
            }
            if(StringUtil.isEmpty(type)){
                type = "考试";
            }

            if (pageNum <= 0 || pageNum > 10000000) {
                pageNum = 1;
            }
            if (pageSize <= 0 || pageSize > 1000) {
                pageSize = 10;
            }
            List<ExperimentExam> list;
            ExperimentUser user = userMapper.getOneByAccOrEmail(account, null);
            if (null == user) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "实验考生不存在，请核实后再查询!");
            }
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
            if (user.getUserTag() == ExperimentConstants.USER_TAG_ADMIN) {
                list = examMapper.getAllByType(type);
            } else {
                list = examMapper.getOneByAccountAndType(account, type);
            }
            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("pageNum", objects.getPageNum());
            result.put("pageSize", objects.getPageSize());
            result.put("total", objects.getTotal());
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("查询考生实验记录失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("查询考生实验记录失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "查询考生实验记录失败，系统错误!");
        } finally {
            logger.info("查询考生实验记录结束!");
        }
    }

    /**
     * 下载程序 VR鼻窦
     *
     * @param sign
     * @param request
     * @param response
     */
    @GetMapping(value = "/downloadexe")
    void downloadExe(String sign, HttpServletRequest request, HttpServletResponse response) {
        logger.info("下载VR鼻窦程序");
        down(sign, vrExeName, request, response);
    }

    /**
     * 下载青霉素实验程序
     *
     * @param sign
     * @param request
     * @param response
     */
    @GetMapping(value = "/downloadpenicillin")
    void downloadpenicillin(String sign, HttpServletRequest request, HttpServletResponse response) {
        logger.info("下载青霉素实验程序");
        down(sign, peExeName, request, response);
    }

    private void down(String sign, String experimentName, HttpServletRequest request, HttpServletResponse response){
        try {
            // 判断是否登录
            ExEnum exEnum = ExEnum.getInstance();
            String id = MD5.MD5Encode(request.getSession().getId());
            UserCacheVo cacheVo = (UserCacheVo) exEnum.getUserCache().get(id);
            if(cacheVo == null){
                throw new ExperimentException(ExperimentException.NOT_LOGIN_CODE, "未登陆!");
            }
            // 判断签名是否正确
            String singOnline = MD5.MD5Encode(cacheVo.getAccount());
            if(!StringUtils.equals(sign, singOnline)){
                throw new ExperimentException(ExperimentException.NOT_LOGIN_CODE, "签名不正确!");
            }
            // 每天只容许下载一次
            int count = 0;
            DownloadCacheVo downloadCacheVo = (DownloadCacheVo) exEnum.getDownloadCache().get(cacheVo.getAccount());
            if (downloadCacheVo != null) {
                count += downloadCacheVo.getDownCount();
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = downloadCacheVo.getDownloadDate();
                if (sendDate.plusDays(1L).isAfter(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE,
                            "每天只容许下载一次!");
                }
            }
            downloadFile(experimentName, request, response);
            // 保存下载记录
            DownloadCacheVo downloadCache = new DownloadCacheVo();
            downloadCache.setAccount(cacheVo.getAccount());
            downloadCache.setDownloadDate(LocalDateTime.now());
            downloadCache.setDownCount(count + 1);
            ExEnum.getInstance().getDownloadCache().put(cacheVo.getAccount(), downloadCache);
            logger.info("用户：{}，下载程序次数：{}", cacheVo.getAccount(), count + 1);
            logger.info("下载程序总次数：{}", ExEnum.getInstance().getDownloadCache().size());

        } catch (ExperimentException e) {
            logger.error("下载失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("下载失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "下载失败，系统错误!");
        }
    }

    public String downloadFile(String experimentName, HttpServletRequest request, HttpServletResponse response) {
        //设置文件路径
        File file = new File(exeDownPath + experimentName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + experimentName);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally { // 做关闭操作
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败";
    }



}
