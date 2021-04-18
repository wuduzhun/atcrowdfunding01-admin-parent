package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.util.SendSmsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberController {

    @Autowired
    private ShortMessageProperties messageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
            ){

        // 1.调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if(ResultEntity.FAILED.equals(resultEntity.getResult())){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();

        if(memberPO == null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 2.比较密码
        String userpswdDataBase = memberPO.getUserpswd();
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

        if(!matcheResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 3.创建MemberLoginVo对象存入session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);

        return "redirect:http://localhost/auth/member/to/center/page";

    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 2.拼redis中存储验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        // 3.从redis读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);
        // 4.查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        // 5.如果能够到value则比较表单的验证码和redis的验证码
        String formCode = memberVO.getCode();
        if (!Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        // 6.如果验证码一致，则从redis删除
        redisRemoteService.removeRedisKeyRemote(key);
        // 7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encode = passwordEncoder.encode(userpswd);
        memberVO.setUserpswd(encode);
        // 8.执行保存
        // 创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();
        // 复制属性
        BeanUtils.copyProperties(memberVO, memberPO);
        // 调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMemer(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:/auth/member/to/login/page";
    }

    /*@RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼Redis中存储验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.从Redis中读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);
        // 4.检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

            return "member-reg";
        }

        String redisCode = resultEntity.getData();

        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 5.如果从Redis能够查询到value，则使用比较表单验证码和Redis验证码
        String formCode = memberVO.getCode();

        if (!Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);

            return "member-reg";
        }

        // 6.如果验证码一致，则从Redis中删除
        redisRemoteService.removeRedisKeyRemote(key);

        // 7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String userpswdBeforeEncode = memberVO.getUserpswd();
        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);

        memberVO.setUserpswd(userpswdAfterEncode);

        // 8.执行保存
        // (1)创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        // (2)复制属性
        BeanUtils.copyProperties(memberVO, memberPO);

        // （3）调用远程方法
        ResultEntity<String> saveMemerResultEntity = mySQLRemoteService.saveMemer(memberPO);
        if(ResultEntity.FAILED.equals(saveMemerResultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemerResultEntity.getMessage());

            return "member-reg";
        }

        return "member-login";
}*/


    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum){

        // 1.发送验证码到phoneNum手机
        ResultEntity<String> sendMessageResultEntity = SendSmsUtil.sendShortMessage(
                "LTAI4FbdkzqkqtfnsoogQwtg",
                "IDA7vtahelcK53U75sDTH0iG5VVzzG", phoneNum,
                "SMS_176537839",
                "威少准");

        // 2.判断短信发送结果
        if(ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())){
            // 3.如果发送成功，则将验证码存入Redis
            // (1)从上一步操作的结果中获取随机生成得验证码
            String code = sendMessageResultEntity.getData();
            System.out.println("code = " +code);

            // (2)拼接一个用于在Redis中存储的key
            String key = CrowdConstant.REDIS_CODE_PREFIX +phoneNum;

            // (3)调用远程接口存入Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            // (4)判断结果
            if(ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){

                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        }else {
            return sendMessageResultEntity;
        }
    }
}
