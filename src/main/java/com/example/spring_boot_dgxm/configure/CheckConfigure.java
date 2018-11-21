package com.example.spring_boot_dgxm.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 *配置文件里自定义的两个验证参数，ip和token
 */
@Component
@ConfigurationProperties(prefix = "check.configure")
@Validated
public class CheckConfigure {

    @NotEmpty
    private String ip;

    @NotEmpty
    private String token;

    @NotEmpty
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
