package com.example.spring_boot_dgxm.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *配置文件里自定义的两个验证参数，ip和token
 */
@Component
public class CheckProperties {

    @Value("${check.configure.ip}")
    private String ip;

    @Value("${check.configure.token}")
    private String token;

    @Value("${check.configure.aeskey}")
    private String aeskey;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }
}
