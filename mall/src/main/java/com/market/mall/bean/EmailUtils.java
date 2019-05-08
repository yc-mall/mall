package com.market.mall.bean;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class EmailUtils extends Thread {
    private String senderEmailAddress;
    private String senderEmailPassword;
    private List<String> receiverEmailAddressList;
    private String message;
    private Date date;

    public EmailUtils(String senderEmailAddress, String senderEmailPassword, List<String> receiverEmailAddressList, String message, Date date) {
        this.senderEmailAddress = senderEmailAddress;
        this.senderEmailPassword = senderEmailPassword;
        this.receiverEmailAddressList = receiverEmailAddressList;
        if (message != null)
            this.message = message;
        else
            this.message = "";
        if (date != null)
            this.date = date;
        else
            this.date = new Date();
    }

    @Override
    public void run() {
        super.run();

        Timer timer = new Timer(true);//把与timer关联的线程设为后台线程
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    sendMail(senderEmailAddress, senderEmailPassword, receiverEmailAddressList, message, date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Long delayTime = date.getTime() - new Date().getTime();
        timer.schedule(timerTask, delayTime < 0 ? 0 : delayTime);
    }

    /**
     * @param receiverEmailAddressList 收件人地址列表
     * @param message                  发送的内容
     * @param date                     发送日期
     * @throws Exception
     * @Description 根据给定的收件人列表、消息和发送日期来发送邮件
     */
    private void sendMail(String senderEmailAddress, String senderEmailPassword, List<String> receiverEmailAddressList, String message, Date date) throws Exception {
        // 发件人的邮箱和密码
        // PS:  某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
        //      对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
        //      发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同
        String senderEmailSMTPHost = "";
        if (senderEmailAddress.contains("@aliyun"))
            senderEmailSMTPHost = "smtp.aliyun.com";
        else if (senderEmailAddress.contains("@gmail"))
            senderEmailSMTPHost = "smtp.gmail.com";
        else if (senderEmailAddress.contains("@sina"))
            senderEmailSMTPHost = "smtp.sina.com.cn";
        else if (senderEmailAddress.contains("@tom"))
            senderEmailSMTPHost = "smtp.tom.com";
        else if (senderEmailAddress.contains("@163"))
            senderEmailSMTPHost = "smtp.163.com";
        else if (senderEmailAddress.contains("@126"))
            senderEmailSMTPHost = "smtp.126.com";
        else if (senderEmailAddress.contains("@yahoo"))
            senderEmailSMTPHost = "smtp.mail.yahoo.com";
        else if (senderEmailAddress.contains("@qq"))
            senderEmailSMTPHost = "smtp.qq.com";
        else if (senderEmailAddress.contains("@foxmail"))
            senderEmailSMTPHost = "smtp.foxmail.com";
        else if (senderEmailAddress.contains("@sohu"))
            senderEmailSMTPHost = "smtp.sohu.com";
        else if (senderEmailAddress.contains("@139"))
            senderEmailSMTPHost = "smtp.139.com";
        else
            return;

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                        // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");       // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", senderEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");                // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");          // 开启ssl安全认证

        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                     // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage mimeMessage = createMimeMessage(session, senderEmailAddress, receiverEmailAddressList, message, date);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        transport.connect(senderEmailAddress, senderEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * @param session                  和服务器交互的会话
     * @param senderEmailAddress       发件人的邮箱
     * @param receiverEmailAddressList 收件人的邮箱列表
     * @param message                  发送信息
     * @param date                     发送时间
     * @return
     * @throws Exception
     */
    private MimeMessage createMimeMessage(Session session, String senderEmailAddress, List<String> receiverEmailAddressList, String message, Date date) throws Exception {
        // 1. 创建一封邮件
        MimeMessage mimeMessage = new MimeMessage(session);

        // 2. personal参数表示发件人的姓名
        mimeMessage.setFrom(new InternetAddress(senderEmailAddress, "庚商公司", "UTF-8"));

        List<InternetAddress> internetAddressList = new ArrayList<InternetAddress>();
        for (String a : receiverEmailAddressList) {
            internetAddressList.add(new InternetAddress(a));
        }

        // 3. To: 收件人
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, (InternetAddress[]) internetAddressList.toArray(new InternetAddress[internetAddressList.size()]));

        // 4. Subject: 邮件主题
        mimeMessage.setSubject("来自庚商的新消息", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        mimeMessage.setContent(message, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        if (date != null && date.after(new Date()))
            mimeMessage.setSentDate(date);
        else
            mimeMessage.setSentDate(new Date());

        // 7. 保存设置
        mimeMessage.saveChanges();

        return mimeMessage;
    }
}

