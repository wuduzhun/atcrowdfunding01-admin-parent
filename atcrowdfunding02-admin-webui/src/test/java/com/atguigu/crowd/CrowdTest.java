package com.atguigu.crowd;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;
    @Test
    public void testTx()
    {
        for (int i = 0; i < 230; i++) {
            Admin admin = new Admin(null,"jerry"+i,"123456"+i,"杰瑞","jerry@qq.com",null);
            adminMapper.insert(admin);
        }

    }

    @Test
    public void testSaveRole()
    {
        for (int i = 0; i < 200 ; i++) {
            roleMapper.insert(new Role(null,"role"+i));
        }
    }

    @Test
    public void testLog()
    {
        // 1、获取日志对象,这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        // 2、根据不同日志级别打印日志
        logger.debug("Hello I am Debug level");
        logger.debug("Hello I am Debug level");
        logger.debug("Hello I am Debug level");

        logger.info("Info ......level");
        logger.info("Info ......level");
        logger.info("Info ......level");

        logger.warn("Warn......level");
        logger.warn("Warn......level");
        logger.warn("Warn......level");


        logger.error("Error.......level");
        logger.error("Error.......level");
        logger.error("Error.......level");
    }

    @Test
    public void testInsertAdmin()
    {
        Admin admin = new Admin(null,"tom","123456","汤姆","tom@qq.com",null);
        int count = adminMapper.insert(admin);
        // 如果在实际开发中，所有想查看数值的地方都使用sysout方式打印，会该项目上线带来问题
        // sysout本质上是一个IO操作，通常IO操作是比较消耗性能的，如果项目中sysout很多，那么对性能的影响是比较大的。
        // 即使我们在上线前专门花时间区删除代码中的sysout，也很有可能遗漏，而且非常麻烦。
        // 如果使用日志系统，那么通过日志级别就可以批量的控制信息打印
        System.out.println("count="+count);
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
