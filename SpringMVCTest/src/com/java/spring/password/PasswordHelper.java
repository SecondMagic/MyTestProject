package com.java.spring.password;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.java.spring.model.User;

@Component
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private final int hashIterations = 2;

    public void encryptPassword(User user) {
        // User���������������ֶ�Username��Password
        
        // ���û���ע�����뾭��ɢ���㷨�滻��һ��������������뱣������ݣ�ɢ�й���ʹ������
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes("123test"),hashIterations).toHex();
        user.setPassword(newPassword);
    }
}
