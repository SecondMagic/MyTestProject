package com.springMVC.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.spring.model.User;
/***
 * shiro test
 * @author 
 *
 */
@Controller
@RequestMapping("authc")
public class AuthcController {
    // /authc/** = authc �κ�ͨ������¼���û������Է���
    @RequestMapping("anyuser")
    public String anyuser() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        System.out.println(user.getName()+":"+user.getPassword());
        return "index";
    }

    // /authc/admin = user[admin] ֻ�о߱�admin��ɫ���û��ſ��Է��ʣ��������󽫱��ض�������¼����
    @RequestMapping("admin")
    public String admin() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        System.out.println(user.getName()+":"+user.getPassword());
        return "index";
    }
}
