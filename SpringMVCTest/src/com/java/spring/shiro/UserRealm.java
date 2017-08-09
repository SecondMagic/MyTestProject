package com.java.spring.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.spring.model.Employee;
import com.java.spring.model.User;
import com.java.spring.service.MyBatisService;

@Service
public class UserRealm extends AuthorizingRealm {
    // �û���Ӧ�Ľ�ɫ��Ϣ��Ȩ����Ϣ�����������ݿ��У�ͨ��UserService��ȡ����
    @Autowired
	MyBatisService myBatisService;

    /**
     * �ṩ�û���Ϣ����Ȩ����Ϣ
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // �����û�����ѯ��ǰ�û�ӵ�еĽ�ɫ
        // ����ɫ�����ṩ��info
        Set<String> roleStr=new HashSet<String>();
        roleStr.add("admin");
        authorizationInfo.setRoles(roleStr);
        return authorizationInfo;
    }

    /**
     * �ṩ�˻���Ϣ������֤��Ϣ
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = new User();
        //������ ����ʹ�û������ӿ���Ӧ
        user.setName("P0003501");
        user.setPassword("a43ddf9e242ca2b15e00dc95de359076");
        if("P0003502".equals(username)){
        	user=null;
        }
        if (user == null) {
            // �û����������׳��쳣
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getName(),
        		user.getPassword(), ByteSource.Util.bytes("123test"), getName());
        return authenticationInfo;
    }
}