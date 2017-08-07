package com.java.spring.mapper;

import com.java.spring.model.Employee;
import com.java.spring.model.User;

public interface UserMapper {
	public Employee getUser(String id);
}
