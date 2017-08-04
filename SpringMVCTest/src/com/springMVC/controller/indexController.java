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
		model.addAttribute("demoStr", "��ӭ");
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login(Model model,User user,HttpServletRequest request, 
											HttpServletResponse response,
											RedirectAttributes redirectAttributes)
			throws Exception {
		// TODO Auto-generated method stub
		//test redis �������
		redisTestService.checkRedis(user);
		
		model.addAttribute(user);
		
		//1.session���� return "redirect:/redirectLogin"
		HttpSession session =request.getSession();
		session.setAttribute("user", user);
		
		//2. return "redirect:/redirectLogin2"
		redirectAttributes.addFlashAttribute("user",user);
		
		//ʵ�������-��post-��������controll1-��get-��controll2-��jsp-������������������ˢ�µȲ���ʹ�ñ��ظ��ύ
		return "redirect:/redirectLogin2";
	}
	
	/***
	 * �ض���1��ͨ��session��ȡ�ض���ǰ������
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
	 * �ض���2��ͨ��addFlashAttribute����
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/redirectLogin2")
	public String redirectLogin(Model model){
		if(model.containsAttribute("user")){
			//��֮ǰ��ĵط�ȡ��
		}else{
			//���ݿ��ѯ�ȵ�
		}
		return "main";
	}
	
	/***
	 * CommonsMultipartResolver
	 * MultipartFile
	 * �ϴ��ļ�
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
			//�׳��쳣״̬�룬һ����˵����̳�RuntimeException�����׳�500״̬�룬����ͨ��@ResponseStatus�޸ģ��׳���404�쳣
			throw new HelloNotFoundException();
		}
		return "showPic";
	}
}
