package com.market.mall.service;

import com.market.mall.bean.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by REM on 2019/5/8.
 */
@Service
public class MailServiceImpl implements MailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${mail.fromMail.addr}")
    private String from;
    /**************************************************************************
     * @param senderEmailAddress    发件人的邮箱地址
     * @param senderEmailPassword   发件人的邮箱密码
     **************************************************************************/
    public int sendPasswordEmail(String senderEmailAddress, String senderEmailPassword,String uname,String emailAddress) {

        int pwd = (int) ((Math.random() * 9 + 1) * 100000);
        StringBuilder sb = new StringBuilder();
        sb.append("HELLO DOCTOR." + uname + "WELCOME：<br/><br/>");
        sb.append("YOUR:");
        sb.append("" + pwd + "");
        String message = sb.toString();
        String emailFormat = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        List<String> emailAddressList = new ArrayList<String>();
        //验证邮箱地址是否符合规范
        if (emailAddress != null && Pattern.matches(emailFormat, emailAddress)) {
            emailAddressList.add(emailAddress);
            if (emailAddressList.size() > 0 && Pattern.matches(emailFormat, senderEmailAddress)) {
                EmailUtils emailUtils = new EmailUtils(senderEmailAddress, senderEmailPassword, emailAddressList, message, new Date());
                emailUtils.run();
            }
            return pwd;
        } else {
            return 0;
        }
    }
    public int sendMail(String uname,String to){
        int pwd = (int) ((Math.random() * 9 + 1) * 100000);
        StringBuilder sb = new StringBuilder();
        sb.append("亲爱的" + uname + "您好：<br/><br/>");
        sb.append("您的验证码为。<br/><br/>");
        sb.append("" + pwd + "");
        String content = sb.toString();
        String subject = "验证邮件";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
        return pwd;
    }
}
