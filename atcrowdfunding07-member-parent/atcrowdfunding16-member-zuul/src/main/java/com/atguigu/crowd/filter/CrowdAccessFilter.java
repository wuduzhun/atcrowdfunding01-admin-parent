package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResource;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {

        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {

        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.通过RequestContext对象获取当前请求对象（框架底层是借助Threadload从当前线程获取事先绑定的request对象）
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取servletPath值
        String servletPath = request.getServletPath();

        // 4.根据servletPath判断当前请求是否可以对应可以直接放行的特定功能
        boolean containsResult = AccessPassResource.PASS_RES_SET.contains(servletPath);

        if(containsResult){

            // 5.如果当前请求是可以直接放行的特定功能请求则返回false放行
            return false;
        }

        // 6.判断当前请求是否为静态资源
        // 工具方法返回true：说明当前请求是静态资源，取反为false表示放行不做登录检查
        // 工具方法返回false：说明当前方法不是可以放行的特定请求也不是静态资源，取反为true表示需要登录检查
        return  !AccessPassResource.judgeCurrentServletPathWetherStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {

        // 1.获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 2.获取当前的session对象
        HttpSession session = request.getSession();

        // 3.尝试从Session对象获取已经登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 4.判断LoginMember是否等于null
        if(loginMember == null){

            // 5.从requestContext获取response对象
            HttpServletResponse response = requestContext.getResponse();

            // 6.将提示消息存入Session域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDEN);

            // 7.重定向到auth-consumer工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
