package com.xy.experiment.utils;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.config.VirtualEntity;
import com.xy.experiment.exceptions.ExperimentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Component
public class MailUtil {

    private final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private VirtualEntity entity;

    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private JavaMailSenderImpl createMailSender() {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
//            sender.setHost(ExperimentConstants.HOST);
//            sender.setPort(ExperimentConstants.PORT);
//            sender.setUsername(ExperimentConstants.USERNAME);
//            sender.setPassword(ExperimentConstants.PASSWORD);
//            sender.setDefaultEncoding("Utf-8");
//            Properties props = new Properties();
//            props.setProperty("mail.smtp.timeout", ExperimentConstants.TIME_OUT);
//            props.setProperty("mail.smtp.auth", ExperimentConstants.AUTH);
//            props.setProperty("mail.transport.protocol", ExperimentConstants.PROTOCOL);
//            props.setProperty("mail.smtp.host", ExperimentConstants.HOST);
            sender.setHost(entity.getHost());
            sender.setPort(entity.getPort());
            sender.setUsername(entity.getUsername());
            sender.setPassword(entity.getPassword());
            sender.setDefaultEncoding("Utf-8");
            Properties props = new Properties();
            props.setProperty("mail.smtp.timeout", entity.getTimeout());
            props.setProperty("mail.smtp.auth", entity.getAuth());
            props.setProperty("mail.transport.protocol", entity.getProtocol());
            props.setProperty("mail.smtp.host", entity.getHost());
            sender.setJavaMailProperties(props);
            logger.info("==========>发送邮箱配置邮箱基本信息！");
            return sender;
        } catch (ExperimentException e) {
            logger.error("===============>赋值参数错误!");
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "赋值参数错误!");
        }
    }

    public void sendMail(String to, Map<String, String> paramMap) throws Exception {
        logger.info("==========> 邮件发送入参:to:{}, paramMap:{}",
                to, JSONObject.toJSONString(paramMap));
        try {
            // 1、连接邮件服务器的参数配置
            JavaMailSenderImpl MAIL_SENDER = this.createMailSender();
            // 2、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(MAIL_SENDER.getJavaMailProperties());
            // 设置调试信息在控制台打印出来
            // session.setDebug(true);
            // 3、创建邮件的实例对象
            Message msg = getMimeMessage(session, to, paramMap);
            // 4、根据session对象获取邮件传输对象Transport
            Transport transport = session.getTransport();
            // 设置发件人的账户名和密码
            transport.connect(entity.getForm(), entity.getPassword());
            // 发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人,
            // 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());

            // 5、关闭邮件连接
            transport.close();
        } catch (ExperimentException e) {
            logger.error("=========> 邮件发送异常！");
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "邮件发送异常!");
        } catch (Exception e) {
            logger.error("=========> 邮件发送异常！系统异常!");
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "邮件发送异常!系统异常!");
        } finally {
            logger.info("=========> 邮件发送结束！");
        }
    }

    /**
     * 获得创建一封邮件的实例对象
     *
     * @param session
     * @return
     * @throws MessagingException
     * @throws AddressException
     */
    public MimeMessage getMimeMessage(Session session, String to, Map<String, String> paramMap)
            throws Exception {
        try {
            String path = MailUtil.class.getResource("/").getPath();

            // 1.创建一封邮件的实例对象
            MimeMessage msg = new MimeMessage(session);
            // 2.设置发件人地址
            msg.setFrom(new InternetAddress(entity.getForm()));
            /**
             * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送 MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));

            // 1. 链接1
            MimeBodyPart text = new MimeBodyPart();
            // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
            if (!StringUtils.isBlank(paramMap.get("url"))) {
                // 4.设置邮件主题
                msg.setSubject("邮箱动态码", "UTF-8");
                text.setContent(EmailTemplate.getEmailTemplate(paramMap.get("url")), "text/html;charset=UTF-8");
            }
            // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            mm.setSubType("related"); // 混合关系
            // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
            msg.setContent(mm);
            // 设置邮件的发送时间,默认立即发送
            msg.setSentDate(new Date());
            return msg;
        } catch (ExperimentException e) {
            logger.error("邮件发送内容设置异常!");
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "邮件发送内容设置异常!");
        } catch (Exception e) {
            logger.error("=========> 邮件发送异常！系统异常!");
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "邮件发送内容设置异常!");
        } finally {
            logger.info("=======> 邮件发送内容设置结束！");
        }
    }
}
