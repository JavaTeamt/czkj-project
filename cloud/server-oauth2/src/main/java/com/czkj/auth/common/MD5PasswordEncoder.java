package com.czkj.auth.common;

import com.czkj.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhangqh
 * @date 2020-03-31
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.md5Encode((String)rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.md5Encode((String)rawPassword));
    }

}
