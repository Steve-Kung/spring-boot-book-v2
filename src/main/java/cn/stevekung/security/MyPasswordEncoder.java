package cn.stevekung.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {
    // 决定密码如何编码
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    // 判断未编码的字符串与编码后的字符串是否匹配
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}
