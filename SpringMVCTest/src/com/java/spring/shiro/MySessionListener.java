package com.java.spring.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MySessionListener implements SessionListener{
	@Override  
    public void onStart(Session session) {//�Ự����ʱ����  
        System.out.println("�Ự������" + session.getId());  
    }  
    @Override  
    public void onExpiration(Session session) {//�Ự����ʱ����  
        System.out.println("�Ự���ڣ�" + session.getId());  
    }  
    @Override  
    public void onStop(Session session) {//�˳�/�Ự����ʱ����  
        System.out.println("�Ựֹͣ��" + session.getId());  
    }    
}
