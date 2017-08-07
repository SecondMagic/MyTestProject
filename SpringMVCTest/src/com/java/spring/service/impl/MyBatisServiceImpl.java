package com.java.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.spring.mapper.UserMapper;
import com.java.spring.model.Employee;
import com.java.spring.model.User;
import com.java.spring.service.MyBatisService;

@Service
public class MyBatisServiceImpl implements MyBatisService{
	@Autowired
    UserMapper userMapper;
	
	@Override
	public Employee getUser(String id) {
		// TODO Auto-generated method stub
		return userMapper.getUser(id);
	}

}
