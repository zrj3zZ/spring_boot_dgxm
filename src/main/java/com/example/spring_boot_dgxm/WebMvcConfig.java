package com.example.spring_boot_dgxm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@EnableWebMvc
@Configuration
public class WebMvcConfig  extends WebMvcConfigurerAdapter {

    @Bean
    public CommonInterceptor interceptor() {
        return new CommonInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(interceptor()).addPathPatterns("/**");  ;
        super.addInterceptors(registry);
    }
}
