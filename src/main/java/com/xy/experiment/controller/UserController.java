package com.xy.experiment.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.xy.experiment.cache.ExEnum;
import com.xy.experiment.config.VirtualEntity;
import com.xy.experiment.constants.ExperimentConstants;
import com.xy.experiment.entity.ExperimentUser;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.mapper.ExperimentUserMapper;
import com.xy.experiment.utils.MD5;
import com.xy.experiment.utils.MailUtil;
import com.xy.experiment.utils.RandomStringGenerator;
import com.xy.experiment.utils.ValidationUtils;
import com.xy.experiment.vo.EmailCacheVo;
import com.xy.experiment.vo.UserCacheVo;
import com.xy.experiment.vo.UserVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(value = "/api/exp")
public class UserController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ExperimentUserMapper userMapper;
    @Autowired
    private VirtualEntity entity;

    @RequestMapping(value = "/registeruser", method = RequestMethod.POST)
    void registerUser(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增学生用户数据：{}", JSONObject.toJSONString(userVo));
        try {
            if (StringUtil.isEmpty(userVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生学号不能为空！");
            }
            if (!ValidationUtils.validaEmail(userVo.getEmail())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生邮箱格式错误！");
            }
            if (!ValidationUtils.validaPassword(userVo.getPassword())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE,
                        "必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间！");
            }
            if (StringUtil.isEmpty(userVo.getToken())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "动态验证码不能为空！");
            }
            // 0. 验证码是否有效
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(userVo.getEmail());
            if(emailCacheVo == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码验证失败，请重新发送！");
            } else {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isBefore(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户动态验证码失效！");
                } else if(!emailCacheVo.getToken().equals(userVo.getToken())){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户动态验证码不正确！");
                }
            }
            // 1.查询是否已经存在用户
            ExperimentUser exUser = userMapper.getOneByAccOrEmail(userVo.getAccount().toLowerCase(),
                    userVo.getEmail().toLowerCase());
            if (null != exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户已经存在，请前往登陆");
            }
            // 2.新增用户
            ExperimentUser insertUser = new ExperimentUser();
            BeanUtils.copyProperties(userVo, insertUser);
            insertUser.setAccount(insertUser.getAccount().toLowerCase());
            insertUser.setEmail(insertUser.getEmail().toLowerCase());
            int resNum = userMapper.insert(insertUser);
            if (resNum != 1) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户新增失败！");
            }
            // 3.跳转到登陆界面
            JSONObject result = new JSONObject();
            result.put("result", insertUser.getAccount());
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增用户失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增用户失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增用户失败，系统错误！");
        } finally {
            logger.info("新增用户结束!");
        }
    }

    @RequestMapping(value = "/registeradmin", method = RequestMethod.POST)
    void registerAdmin(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增管理员用户数据：{}", JSONObject.toJSONString(userVo));
        try {
            if (StringUtil.isEmpty(userVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "登录名称不能为空！");
            }
            if (!ValidationUtils.validaEmail(userVo.getEmail())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "邮箱格式错误！");
            }
            if (!ValidationUtils.validaPassword(userVo.getPassword())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE,
                        "必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间！");
            }
            if (StringUtil.isEmpty(userVo.getToken())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "动态验证码不能为空！");
            }
            // 0. 验证码是否有效
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(userVo.getEmail());
            if(emailCacheVo == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码验证失败，请重新发送！");
            } else {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isBefore(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户动态验证码失效！");
                } else if(!emailCacheVo.getToken().equals(userVo.getToken())){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户动态验证码不正确！");
                }
            }
            // 1.0 邮箱是否容许注册，只有规定邮箱才可以注册

            // 1.查询是否已经存在用户
            ExperimentUser exUser = userMapper.getOneByAccOrEmail(userVo.getAccount().toLowerCase(),
                    userVo.getEmail().toLowerCase());
            if (null != exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户已经存在，请前往登陆");
            }
            // 2.新增用户
            ExperimentUser insertUser = new ExperimentUser();
            BeanUtils.copyProperties(userVo, insertUser);
            insertUser.setAccount(insertUser.getAccount().toLowerCase());
            insertUser.setEmail(insertUser.getEmail().toLowerCase());
            insertUser.setUserTag(ExperimentConstants.USER_TAG_ADMIN);
            int resNum = userMapper.insert(insertUser);
            if (resNum != 1) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户新增失败！");
            }
            // 3.跳转到登陆界面
            JSONObject result = new JSONObject();
            result.put("result", insertUser.getAccount());
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("新增用户失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("新增用户失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "新增用户失败，系统错误！");
        } finally {
            logger.info("新增用户结束!");
        }
    }

    @RequestMapping(value = "/loginuser", method = RequestMethod.POST)
    void loginUser(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户登陆数据：{}", JSONObject.toJSONString(userVo));
        try {
            if (StringUtil.isEmpty(userVo.getAccount())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生学号不能为空！");
            }
            if (!ValidationUtils.validaPassword(userVo.getPassword())) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE,
                        "必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间！");
            }
            // 1.查询是否已经存在用户
            ExperimentUser exUser = userMapper.getOneByAccOrEmail(userVo.getAccount().toLowerCase(),
                    userVo.getAccount().toLowerCase());
            if (null == exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户未注册，请先注册用户！");
            }
            // 2.验证密码正确性
            if (!StringUtils.equals(exUser.getPassword(), userVo.getPassword())) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户账号密码不匹配！");
            }
            // 3.加入登陆标识
            ExEnum exEnum = ExEnum.getInstance();
            UserCacheVo cacheVo = new UserCacheVo();
            cacheVo.setAccount(userVo.getAccount());
            cacheVo.setLoginDate(new Date());
            cacheVo.setObject(request.getSession());
            cacheVo.setSessionId(MD5.MD5Encode(request.getSession().getId()));
            // 登陆session超过5w个，则删除部分登陆时长超过24小时的
            if(exEnum.getUserCache().size() >= 50000){
                Iterator<Map.Entry<String, Object>> it = exEnum.getUserCache().entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    //使用迭代器的remove()方法删除元素
                    String mapKey = entry.getKey();
                    UserCacheVo mapValue = (UserCacheVo) entry.getValue();
                    Date nowDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nowDate);
                    calendar.add(Calendar.DATE, -1);
                    nowDate = calendar.getTime();
                    if(nowDate.after(mapValue.getLoginDate())){
                        it.remove();
                    }
                }
                logger.info("==================>清除后的session集合大小:{}", exEnum.getUserCache().size());
            }
            exEnum.getUserCache().put(cacheVo.getSessionId(), cacheVo);

            // 4.跳转到主界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("用户登陆失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("用户登陆失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "用户登陆失败，系统错误！");
        } finally {
            logger.info("用户登陆结束!");
        }
    }

    @RequestMapping(value = "/islogin", method = RequestMethod.POST)
    void isLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("验证用户是否登陆开始");
        try {
            // 1.是否已经登陆标识
            ExEnum exEnum = ExEnum.getInstance();
            String id = MD5.MD5Encode(request.getSession().getId());
            UserCacheVo cacheVo = (UserCacheVo) exEnum.getUserCache().get(id);
            if(cacheVo == null){
                throw new ExperimentException(ExperimentException.NOT_LOGIN_CODE, "未登陆!");
            }
            // 2.跳转到主界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("验证用户是否登陆失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("验证用户是否登陆失败!", e);
            sendFailureMessage(response, ExperimentException.NOT_LOGIN_CODE,
                    "验证用户是否登陆失败，系统错误！");
        } finally {
            logger.info("验证用户是否登陆完成!");
        }
    }

    @RequestMapping(value = "/sendemail", method = RequestMethod.POST)
    void sendEmail(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户发送邮箱动态验证码参数：邮箱：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String email = req.getString("email");
            if (!ValidationUtils.validaEmail(email)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "邮箱格式错误！");
            }
            String token = RandomStringGenerator.getRandomStringByLength(4);
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(email);
            if (emailCacheVo != null) {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isAfter(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE,
                            "用户动态验证码连接已经发送, 并在有效期内！");
                }
            }
            // 数据库查询，是否已经存在用户邮箱
            ExperimentUser user = userMapper.getOneByEmail(email.toLowerCase());
            if(user == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户未注册，请先注册!");
            }
            // 1.发送邮箱
            MailUtil mailUtil = new MailUtil();
            Map<String, String> params = new HashMap();
            params.put("url", token);
            mailUtil.sendMail(email, params);

            // 2.保存邮箱发送，防止重复发送重置邮箱
            EmailCacheVo cacheVo = new EmailCacheVo();
            cacheVo.setEmail(email);
            cacheVo.setSendEmailDate(LocalDateTime.now());
            cacheVo.setToken(token);
            ExEnum.getInstance().getEmailCache().put(email, cacheVo);

            // 3.跳转到发送成功界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("发送邮箱失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("发送邮箱失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "发送邮箱失败，系统错误！");
        } finally {
            logger.info("发送邮箱结束!");
        }
    }

    @RequestMapping(value = "/sendverify", method = RequestMethod.POST)
    void sendVerify(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户发送注册邮箱动态验证码参数：邮箱：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String email = req.getString("email");
            if (!ValidationUtils.validaEmail(email)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "邮箱格式错误！");
            }
            String token = RandomStringGenerator.getRandomStringByLength(4);
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(email);
            if (emailCacheVo != null) {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isAfter(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE,
                            "用户动态验证码连接已经发送, 并在有效期内！");
                }
            }
            // 数据库查询，是否已经存在用户邮箱
            ExperimentUser user = userMapper.getOneByEmail(email.toLowerCase());
            if(user != null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户已注册，请登录!");
            }
            // 1.发送邮箱
            MailUtil mailUtil = new MailUtil();
            Map<String, String> params = new HashMap();
            params.put("url", token);
            mailUtil.sendMail(email, params);

            // 2.保存邮箱发送，防止重复发送重置邮箱
            EmailCacheVo cacheVo = new EmailCacheVo();
            cacheVo.setEmail(email);
            cacheVo.setSendEmailDate(LocalDateTime.now());
            cacheVo.setToken(token);
            ExEnum.getInstance().getEmailCache().put(email, cacheVo);

            // 3.跳转到发送成功界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("发送邮箱失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("发送邮箱失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "发送邮箱失败，系统错误！");
        } finally {
            logger.info("发送邮箱结束!");
        }
    }

    @RequestMapping(value = "/sendupdateemail", method = RequestMethod.POST)
    void sendUpdateEmail(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户发送邮箱动态验证码参数：邮箱：{}", reqJsonStr);
        try {
            reqJsonStr = URLDecoder.decode(reqJsonStr, "utf-8");
            JSONObject req = JSONObject.parseObject(reqJsonStr);
            String email = req.getString("email");
            if (!ValidationUtils.validaEmail(email)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "邮箱格式错误！");
            }
            String token = RandomStringGenerator.getRandomStringByLength(4);
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(email);
            if (emailCacheVo != null) {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isAfter(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE,
                            "用户动态验证码连接已经发送, 并在有效期内！");
                }
            }
            ExperimentUser user = userMapper.getOneByEmail(email.toLowerCase());
            if(user == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户未注册，请先注册!");
            }
            // 1.发送邮箱
            MailUtil mailUtil = new MailUtil();
            Map<String, String> params = new HashMap();
            params.put("url", token);
            mailUtil.sendMail(email, params);

            // 2.保存邮箱发送，防止重复发送重置邮箱
            EmailCacheVo cacheVo = new EmailCacheVo();
            cacheVo.setEmail(email);
            cacheVo.setSendEmailDate(LocalDateTime.now());
            cacheVo.setToken(token);
            ExEnum.getInstance().getEmailCache().put(email, cacheVo);

            // 3.跳转到发送成功界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("发送邮箱失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("发送邮箱失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "发送邮箱失败，系统错误！");
        } finally {
            logger.info("发送邮箱结束!");
        }
    }

    @RequestMapping(value = "/validemail", method = RequestMethod.POST)
    void validEmail(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户发送邮箱动态验证码参数：邮箱：{}", reqJsonStr);
        try {
            JSONObject reqJson = JSONObject.parseObject(URLDecoder.decode(reqJsonStr, "utf-8"));
            String email = reqJson.getString("email");
            String token = reqJson.getString("token");
            if (StringUtil.isEmpty(token)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "动态验证码不能为空！");
            }
            if (!ValidationUtils.validaEmail(email)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "邮箱格式错误！");
            }
            EmailCacheVo emailCacheVo = (EmailCacheVo) ExEnum.getInstance().getEmailCache().get(email);
            if(emailCacheVo == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码验证失败，请重新发送！");
            }
            if (emailCacheVo != null) {
                LocalDateTime nowDate = LocalDateTime.now();
                LocalDateTime sendDate = emailCacheVo.getSendEmailDate();
                if (sendDate.plusMinutes(10).isBefore(nowDate)){
                    throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户动态验证码失效！");
                }
            }
            // 3.验证成功
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("发送邮箱失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("发送邮箱失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "发送邮箱失败，系统错误！");
        } finally {
            logger.info("发送邮箱结束!");
        }
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    void updatePassWord(@RequestBody String reqJsonStr, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户更新密码数据：reqJsonStr:{}",reqJsonStr);
        try {
            JSONObject reqJson = JSONObject.parseObject(URLDecoder.decode(reqJsonStr, "utf-8"));
            String account = reqJson.getString("account");
            String email = reqJson.getString("email");
            String password = reqJson.getString("password");
            String token = reqJson.getString("token");
            if (StringUtil.isEmpty(email)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE, "学生学号对应邮箱错误！");
            }
            if (!ValidationUtils.validaPassword(password)) {
                throw new ExperimentException(ExperimentException.PARAMS_ERROR_CODE,
                        "必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间！");
            }
            // 1.验证token时效性
            Map<String, Object> emailCache = ExEnum.getInstance().getEmailCache();
            EmailCacheVo cacheVo = (EmailCacheVo) emailCache.get(email);
            if(cacheVo == null){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码链接失效！请重新发送邮箱！");
            }
            LocalDateTime sendEmailDate = cacheVo.getSendEmailDate();
            LocalDateTime nowDate = LocalDateTime.now();
            if(sendEmailDate.plusMinutes(10).isBefore(nowDate)){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码链接失效！请重新发送邮箱！");
            }
            if(!token.equals(cacheVo.getToken())){
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "动态验证码错误！");
            }
            emailCache.remove(email);

            // 2.查询是否已经存在用户
            ExperimentUser exUser = userMapper.getOneByEmail(email.toLowerCase());
            if (null == exUser) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "用户未注册，请先注册用户！");
            }

            // 3.修改密码
            exUser.setPassword(password);
            userMapper.update(exUser);

            // 4.跳转到主界面
            JSONObject result = new JSONObject();
            result.put("result", "SUCCESS");
            sendSuccessData(response, result);
        } catch (ExperimentException e) {
            logger.error("用户更新密码失败: code:{}, msg:{}", e.getCode(), e);
            sendFailureMessage(response, e.getCode(), e.getMsg());
        } catch (Exception e) {
            logger.error("用户更新密码失败!", e);
            sendFailureMessage(response, ExperimentException.SYSTEM_ERROR_CODE, "用户更新密码失败，系统错误！");
        } finally {
            logger.info("用户更新密码结束!");
        }
    }
}
