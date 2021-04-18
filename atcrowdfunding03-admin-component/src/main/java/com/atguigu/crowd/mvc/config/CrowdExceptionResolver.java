package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUupdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.constant.CrowdConstant;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ControllerAdvice表示当前类是一个基于注解的异常处理器类
 */
@ControllerAdvice
public class CrowdExceptionResolver
{
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUupdateException.class)
    public ModelAndView resolverLoginAcctAlreadyInUseForUupdateException(
            // 实际捕获到的异常类型
            LoginAcctAlreadyInUseForUupdateException exception,
            // 当前请求对象
            HttpServletResponse response,
            //当前响应对象
            HttpServletRequest request) throws IOException {

        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolverLoginAcctAlreadyInUseException(
            // 实际捕获到的异常类型
            LoginAcctAlreadyInUseException exception,
            // 当前请求对象
            HttpServletResponse response,
            //当前响应对象
            HttpServletRequest request) throws IOException {

        String viewName = "admin-add";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolverException(
            // 实际捕获到的异常类型
            Exception exception,
            // 当前请求对象
            HttpServletResponse response,
            //当前响应对象
            HttpServletRequest request) throws IOException {

        String viewName = "admin-login";
        return commonResolve(viewName, exception, request, response);
    }


    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolverArithmeticException(
            // 实际捕获到的异常类型
            ArithmeticException exception,
            // 当前请求对象
            HttpServletResponse response,
            //当前响应对象
            HttpServletRequest request) throws IOException {

        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    /*
    @ExceptionHandler 讲一个具体的异常类型和方法关联起来
     */
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView resolverNullPointerException(
            // 实际捕获到的异常类型
            NullPointerException exception,
            // 当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    private ModelAndView commonResolve(
            //处理异常完要去的页面
            String viewName,
            // 实际捕获到的异常类型
            Exception exception,
            // 当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {
        // 1.判断当前请求类型
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);

        // 2.如果是Ajac请求
        if(judgeRequestType){
            // 3.创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            // 4.创建Gson
            Gson gson = new Gson();

            // 5.将ResultEntity对象转换成JSON字符串
            String json = gson.toJson(resultEntity);

            // 6.将JSON字符串作为响应体返回浏览器
            response.getWriter().write(json);

            // 7.由于上面通过原生response对象返回了响应体，所以不提供ModelAndView对象
            return null;
        }

        // 8.如果不是Ajax请求创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        // 9.将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);

        // 10.设置对应的视图名称
        modelAndView.setViewName(viewName);

        // 11.返回modelAndView对象
        return modelAndView;
    }
}
