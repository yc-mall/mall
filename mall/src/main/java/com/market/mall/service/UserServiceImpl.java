package com.market.mall.service;

import com.market.mall.bean.BeanUtils;
import com.market.mall.entity.User;
import com.market.mall.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by REM on 2019/5/6.
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    public boolean inserUser(HttpServletRequest request) {
        User user= BeanUtils.asBean(request, User.class);
        userMapper.insert(user);
        return true;
    }
    public User login(String uname,String pwd){
        return userMapper.login(uname,pwd);
    }
    public User selectByUname(String uname){
        return userMapper.selectByUname(uname);
    }
}
