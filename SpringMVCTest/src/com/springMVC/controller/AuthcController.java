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
    // /authc/** = authc 任何通过表单登录的用户都可以访问
    @RequestMapping("anyuser")
    public String anyuser() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        System.out.println(user.getName()+":"+user.getPassword());
        return "index";
    }

    // /authc/admin = user[admin] 只有具备admin角色的用户才可以访问，否则请求将被重定向至登录界面
    @RequestMapping("admin")
    public String admin() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        System.out.println(user.getName()+":"+user.getPassword());
        return "index";
    }
}
