package com.market.mall.service;

import com.market.mall.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by REM on 2019/5/6.
 */
public interface UserService {
    public boolean inserUser(HttpServletRequest request);

    public User login(String uname,String pwd);

    public User selectByUname(String uname);


}
