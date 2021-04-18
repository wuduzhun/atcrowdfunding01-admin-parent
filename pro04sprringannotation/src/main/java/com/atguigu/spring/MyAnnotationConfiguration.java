package com.atguigu.spring;

import com.atguigu.Entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 表示当前这个类是一个配置类，作用大致相当于以前的spring-context.xml这样的配置文件
@Configuration
public class MyAnnotationConfiguration
{
    // @Bean注解相当于做了下面xml标签的配置，把方法返回值放入IOC容器
    // <bean id="emp" class="com.atguigu.Entity.Employee"/>
    @Bean
    public Employee getEmployee()
    {
        return new Employee();
    }
}
