package com.atguigu.crowd.test;

import com.atguigu.Entity.Employee;
import com.atguigu.spring.MyAnnotationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest
{
    public static void main(String[] args)
    {
        // 以前new ClassPathXmlApplicationContext("");方式加载XML配置文件

        // 基于注解配置类创建IOC容器对象
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(MyAnnotationConfiguration.class);

        // 从IOC容器获取bean
        Employee employee = applicationContext.getBean(Employee.class);

        System.out.println(employee);

        applicationContext.close();
    }

}
