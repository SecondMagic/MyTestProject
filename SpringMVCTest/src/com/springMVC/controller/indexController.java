package com.springMVC.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.spring.model.Employee;
import com.java.spring.model.User;
import com.java.spring.password.PasswordHelper;
import com.java.spring.service.HelloSpringService;
import com.java.spring.service.MyBatisService;
import com.java.spring.service.RedisTestService;
import com.java.spring.web.HelloNotFoundException;

@Controller
public class indexController{
	@Autowired
	@Qualifier("HelloSpringService") 
	HelloSpringService helloSpringService;
	
	@Autowired
	@Qualifier("HelloSpringServiceT") 
	HelloSpringService helloSpringServiceT;
	
	@Autowired
	RedisTestService redisTestService;
	
	@Autowired
	MyBatisService myBatisService;
	
	@Autowired
	PasswordHelper passwordHelper;
	
	@RequestMapping( value="/index")
	public String helloSpring(Model model)
			throws Exception {
		// TODO Auto-generated method stub
//		ModelAndView model=new ModelAndView();
		//aop测试
		System.out.println("AOP Test start -----------------------");
		helloSpringService.printHello("123");
		helloSpringServiceT.printHello("456");
		System.out.println("AOP Test end -------------------------");
		model.addAttribute("demoStr", "欢迎");
		
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login(Model model,User user,HttpServletRequest request, 
											HttpServletResponse response,
											RedirectAttributes redirectAttributes)
			throws Exception {
		// TODO Auto-generated method stub
		
		//shiro登入测试
		//创建令牌
//		passwordHelper.encryptPassword(user);
		UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
		Subject subject = SecurityUtils.getSubject();
		System.out.println("subject.isAuthenticated:"+subject.isAuthenticated());
		System.out.println("subject.isRemembered:"+subject.isRemembered());
		try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // 捕获密码错误异常
        	System.out.println("捕获密码错误异常");
            return "index";
        } catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
        	System.out.println("捕获未知用户名异常");
            return "index";
        } catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
        	System.out.println("捕获错误登录过多的异常");
            return "index";
        }
		//shiro session,Shiro管理的会话对象，要获取依然必须通过Shiro。传统的Session中不存在User对象
		subject.getSession().setAttribute("user", user);
//		subject.isAuthenticated();
		System.out.println("subject.isAuthenticated:"+subject.isAuthenticated());
		System.out.println("subject.isRemembered:"+subject.isRemembered());
		
		//MyBatis测试
		Employee employee=myBatisService.getUser(user.getName());
		System.out.println("MyBatis:"+employee.getName());
		
		//test redis 缓存测试
		User user2=user;
		user=null;
		user=redisTestService.checkRedis(user2);
		
		//..................................
		model.addAttribute(user);
		
		//重定向测试，post->重定向->get
		//1.session保存 return "redirect:/redirectLogin"
		HttpSession session =request.getSession();
		session.setAttribute("user", user);
		
		//2. return "redirect:/redirectLogin2"
		redirectAttributes.addFlashAttribute("user",user);
		
		//实现浏览器-》post-》服务器controll1-》get-》controll2-》jsp-》浏览器，避免浏览器刷新等操作使得表单重复提交
		return "redirect:/redirectLogin2";
	}
	
	/***
	 * 重定向1，通过session获取重定向前的数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/redirectLogin")
	public String redirectLogin(Model model,HttpServletRequest request, HttpServletResponse response){
		HttpSession session=request.getSession();
		model.addAttribute((User)session.getAttribute("user"));
//		session.removeAttribute("user");
		return "main";
	}
	
	/***
	 * 重定向2，通过addFlashAttribute传递
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/redirectLogin2")
	public String redirectLogin(Model model){
		if(model.containsAttribute("user")){
			//从之前存的地方取出
		}else{
			//数据库查询等等
		}
		return "main";
	}
	
	/***
	 * CommonsMultipartResolver
	 * MultipartFile
	 * 在spring-shiro-web中设置了该请求会先检查是否已登入，如果未登录则返回index页面
	 * 上传文件
	 * @param clientFile
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/uploadFile")
	public String uploadFile(@RequestParam("pic") MultipartFile clientFile,HttpServletRequest request,Model model) throws IOException{
		String name = null,suffix = null,allName = null,fileName,path;
		if(!clientFile.isEmpty()){
			name=clientFile.getName();
			suffix=clientFile.getContentType();
			allName=clientFile.getOriginalFilename();			
			path=request.getServletContext().getRealPath("/");			
			fileName=path+"/WEB-INF/file/"+allName;
			
			model.addAttribute("allName", allName);
			
			File imageFile = new File(fileName); 	
			try {
				clientFile.transferTo(imageFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//抛出异常状态码，一般来说这里继承RuntimeException，会抛出500状态码，但是通过@ResponseStatus修改，抛出了404异常
			//throw new HelloNotFoundException();
		}
		return "showPic";
	}
	
	@RequestMapping("/logout")
	public String logout(Model model){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/index";
	}
	
	@RequestMapping("/view")
	public String view(Model model){
		Subject subject = SecurityUtils.getSubject();
		User user=(User)subject.getSession().getAttribute("user");
		model.addAttribute(user);
		
		//Test session
//        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
//        CacheManager cacheManager=securityManager.getCacheManager();
//        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
//        Collection<Session> sessions1 = sessionManager.getSessionDAO().getActiveSessions();
//        Collection<Object> cache = cacheManager.getCache("shiro-activeSessionCache").values();
//        System.out.println("cache:"+cache);
		return "view";
	}
}
