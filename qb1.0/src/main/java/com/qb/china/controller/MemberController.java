package com.qb.china.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qb.china.domain.User;
import com.qb.china.service.UserService;

/**
 * 会员
 * Create by ML on 2016年9月19日.
 */
@Controller
@RequestMapping("/user")
public class MemberController {
	@Resource
    private UserService userService;
	
	/** 会员主页*/
	@RequestMapping("/index")
	public String index() {
		
		return "member/index";
	}
	
	@RequestMapping("/showUser")
	public String showUser(HttpServletRequest request,Model model){
		int userId = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserById(userId);
		model.addAttribute("user", user);
		return "member/showUser";
	}
}
