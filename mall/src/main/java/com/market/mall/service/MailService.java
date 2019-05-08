package com.market.mall.service;

/**
 * Created by REM on 2019/5/8.
 */
public interface MailService {
    public int sendPasswordEmail(String senderEmailAddress, String senderEmailPassword,String uname,String emailAddress);
    public int sendMail(String uname,String to);
}
