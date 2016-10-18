package com.qb.china.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qb.china.dao.UserDao;
import com.qb.china.domain.User;
import com.qb.china.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserDao userDao;

	public User getUserById(int userId) {
		return userDao.selectByPrimaryKey(userId);
	}

}
