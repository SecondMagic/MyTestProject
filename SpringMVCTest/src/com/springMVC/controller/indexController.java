package com.springMVC.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.java.spring.model.User;
import com.java.spring.service.HelloSpringService;
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
	
	@RequestMapping( value="/index")
	public String helloSpring(Model model)
			throws Exception {
		// TODO Auto-generated method stub
//		ModelAndView model=new ModelAndView();
		helloSpringService.printHello("123");
		helloSpringServiceT.printHello("456");
		model.addAttribute("demoStr", "欢迎");
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login(Model model,User user,HttpServletRequest request, 
											HttpServletResponse response,
											RedirectAttributes redirectAttributes)
			throws Exception {
		// TODO Auto-generated method stub
		//test redis 缓存测试
		redisTestService.checkRedis(user);
		
		model.addAttribute(user);
		
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
			throw new HelloNotFoundException();
		}
		return "showPic";
	}
}
