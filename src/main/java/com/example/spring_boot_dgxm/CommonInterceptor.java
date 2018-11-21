package com.example.spring_boot_dgxm;

import com.example.spring_boot_dgxm.configure.CheckProperties;
import com.example.spring_boot_dgxm.configure.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class CommonInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private CheckProperties checkProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ip = IPUtils.getRealIP(request);

        boolean flag=false;
        if ("GET".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod())) {
            String token = request.getParameter("token");
            if (token != null && !"".equals(token)) {
                String tokenSource = checkProperties.getToken();
                if (!token.equals(tokenSource)) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer;
                    writer = response.getWriter();
                    writer.write("token验证不通过！");
                    return false;
                }
            } else {
                throw new Exception("token不能为空！");
            }
            if("0:0:0:0:0:0:0:1".equals(ip)){
                return true;
            }
            String ipSource=checkProperties.getIp();
            if(!"*".equals(ipSource)){
                String[] ips=ipSource.split(",");
                for (int i=0;i<ips.length;i++){
                    String zsip=ips[i];
                    if(zsip.contains("*")){
                        zsip=zsip.substring(0,zsip.lastIndexOf("."));
                        ip=ip.substring(0,ip.lastIndexOf("."));
                        if(zsip.equals(ip)){
                            flag=true;
                            break;
                        }
                    }else{
                        if(zsip.equals(ip)){
                            flag=true;
                            break;
                        }
                    }
                }
            }else{
                flag=true;
            }
            if("0:0:0:0:0:0:0:1".equals(ip)) flag=true;
            if(!flag){
                throw new Exception("该ip："+ip+"，限制访问！");
            }
            return true;
        } else {
            throw new Exception("不支持的请求类型！");
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
