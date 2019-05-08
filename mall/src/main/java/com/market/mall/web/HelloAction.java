package com.market.mall.web;

import com.market.mall.entity.User;
import com.market.mall.service.MailService;
import com.market.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloAction {
	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	@RequestMapping("/index")
	public String index(){
		return "head";
	}
	@RequestMapping("/regist")
	public String regist(HttpServletRequest request){
		userService.inserUser(request);
		return "head";
	}
	@RequestMapping("/login")
	public String login(HttpServletRequest request){
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		User user = userService.login(uname,upwd);
		if (user!=null){
			request.getSession().setAttribute("user",user);
		}
		return "head";
	}
	@RequestMapping("/checkuname")
	@ResponseBody
	public String checkuname(String uname){
		User user = userService.selectByUname(uname);
		if (user==null){
			return "该用户名可用";
		}else {
			return "该用户名已被占用";
		}
	}
	@RequestMapping("/sendMail")
	@ResponseBody
	public int sendMail(String uname,String mailAddr){
		return mailService.sendMail(uname,mailAddr);
	}

}
