package com.java.spring.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.java.spring.model.User;
import com.java.spring.service.RedisTestService;

@Service
public class RedisTestServiceImpl implements RedisTestService{

	/***
	 * redisª∫¥Ê≤‚ ‘
	 * @param user
	 * @return
	 */
	@Cacheable(value="x",key="#root.args[0].name+#root.args[0].password")
	@Override
	public User checkRedis(User user){
		System.out.println("use redis:"+user.getName()+"+"+user.getPassword());
		return user;
	}
}
