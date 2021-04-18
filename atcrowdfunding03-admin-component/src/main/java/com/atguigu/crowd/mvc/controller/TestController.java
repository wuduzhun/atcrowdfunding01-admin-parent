package com.atguigu.crowd.mvc.controller;

import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.ParamData;
import com.atguigu.crowd.entity.Student;
import com.atguigu.crowd.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestController.class);


    @ResponseBody
    @RequestMapping("/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request)
    {
        boolean judgeRequest = CrowdUtil.judgeRequestType(request);
        logger.info("judgeRequest = " + judgeRequest);
        logger.info(student.toString());
        String a = null;
        System.out.println(a.length());
        // 将“查询”到的Student对象封装到ResultEntity中返回
        return ResultEntity.successWithData(student);
    }


    @ResponseBody
    @RequestMapping("/send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array)
    {

        for (Integer number : array) {
            System.out.println(number);
        }
        return "success";
    }
    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String testReceiveArrayTwo(ParamData paramData)
    {
        List<Integer> array = paramData.getArray();
        for (Integer number : array) {
            System.out.println(number);
        }
        return "success";
    }
    @ResponseBody
    @RequestMapping("/send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array)
    {
        for (Integer number : array) {
            logger.info("number = " + number);
        }
        return "success";
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request)
    {
        boolean judgeRequest = CrowdUtil.judgeRequestType(request);
        logger.info("judgeRequest = " + judgeRequest);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);




        return "target";
    }

    @ResponseBody
    @RequestMapping("/test/ajax/async.html")
    public String sendAsyunc() throws InterruptedException {
        Thread.sleep(200);
        return "success";
    }
}
