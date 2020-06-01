package com.linchtech.boot.starter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送工具
 * @author 107
 * @date 2019/6/27 9:30
 */
@Component
@Slf4j
public class EmailUtil {

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value(("${spring.mail.username}"))
    private String username;

    @Value("${spring.mail.enable}")
    private Boolean enable;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * javax mail邮件发送
     *
     * @param sendTo  发送给
     * @param subject 主题
     * @param content 内容
     * @param file    附件
     * @throws MessagingException
     */
    public void send(List<String> sendTo,
                     String subject,
                     String content,
                     File file) throws MessagingException, IOException {
        // 第一步：创建Session，包含邮件服务器网络连接信息
        Properties props = new Properties();
        // 指定邮件的传输协议，smtp;同时通过验证
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);

        // 第二步：编辑邮件内容
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, "*", "UTF-8"));
        message.setSubject(subject);
        // 拼接邮箱地址，以逗号隔开
        StringBuilder stringbuilder = new StringBuilder();
        for (String s : sendTo) {
            stringbuilder.append(s).append(",");
        }
        if (stringbuilder.length() > 0) {
            stringbuilder.deleteCharAt(stringbuilder.length() - 1);
        }
        // 发送给多个接收人
        InternetAddress[] internetAddressTo = InternetAddress.parse(stringbuilder.toString());
        message.setRecipients(Message.RecipientType.TO, internetAddressTo);
        // 设置邮件消息内容、包含附件
        Multipart msgPart = new MimeMultipart();
        message.setContent(msgPart);
        // 正文
        MimeBodyPart body = new MimeBodyPart();
        msgPart.addBodyPart(body);
        // 设置正文内容
        body.setContent(content, "text/html;charset=utf-8");
        if (file != null) {
            // 附件
            BodyPart attach = new MimeBodyPart();
            attach.setDataHandler(new DataHandler(new FileDataSource(file)));
            // 设置附件内容
            attach.setFileName(MimeUtility.encodeText(file.getName()));
            msgPart.addBodyPart(attach);
        }
        message.setContent(msgPart);
        message.saveChanges();
        // 第三步：发送邮件
        Transport trans = session.getTransport();
        trans.connect(host, username, password);
        trans.sendMessage(message, message.getAllRecipients());

    }

    /**
     * 将MultipartFile转换为file类型
     *
     * @param file
     * @return
     * @throws IOException
     */
    public File transformToFile(MultipartFile file) throws IOException {
        File f = new File(file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), f);
        return f;
    }

    /**
     * 使用spring mail发送邮件
     *
     * @param to      发送给
     * @param subject 主题
     * @param content 内容
     * @param file    附件
     */
    @Async
    public void sendBySpringMail(List<String> to, String subject, String content, MultipartFile file) {
        if (enable) {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(username);
                String[] strings = new String[to.size()];
                helper.setTo(to.toArray(strings));
                helper.setSubject(subject);
                helper.setText(content, true);
                if (file != null) {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
                mailSender.send(message);
                log.info("邮件已经发送。");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

}
