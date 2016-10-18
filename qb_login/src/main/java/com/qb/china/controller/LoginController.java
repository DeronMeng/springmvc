package com.qb.china.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录
 * Create by Long.Meng on 2016年9月19日.
 */
@Controller
@RequestMapping("/")
public class LoginController {

	@RequestMapping("/login")
	public String login() {
		
		return "login";
	}
}
