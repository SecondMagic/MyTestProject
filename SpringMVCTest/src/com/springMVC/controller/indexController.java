package com.springMVC.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
		//aop����
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
		
		//shiro�������
		//��������
//		passwordHelper.encryptPassword(user);
		UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
		Subject subject = SecurityUtils.getSubject();
		try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // ������������쳣
        	System.out.println("������������쳣");
            return "index";
        } catch (UnknownAccountException uae) {
            // ����δ֪�û����쳣
        	System.out.println("����δ֪�û����쳣");
            return "index";
        } catch (ExcessiveAttemptsException eae) {
            // ��������¼������쳣
        	System.out.println("��������¼������쳣");
            return "index";
        }
		//shiro session,Shiro����ĻỰ����Ҫ��ȡ��Ȼ����ͨ��Shiro����ͳ��Session�в�����User����
		subject.getSession().setAttribute("user", user);
		
		//MyBatis����
		Employee employee=myBatisService.getUser(user.getName());
		System.out.println("MyBatis:"+employee.getName());
		
		//test redis �������
		User user2=user;
		user=null;
		user=redisTestService.checkRedis(user2);
		
		//..................................
		model.addAttribute(user);
		
		//�ض�����ԣ�post->�ض���->get
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
	 * ��spring-shiro-web�������˸�������ȼ���Ƿ��ѵ��룬���δ��¼�򷵻�indexҳ��
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
