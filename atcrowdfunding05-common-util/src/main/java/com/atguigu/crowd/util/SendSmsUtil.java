package com.atguigu.crowd.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSmsUtil {
    public static ResultEntity<String> sendShortMessage(
            String accessKeyId,
            String accessSecret,
            String phoneNum,
            String templateCode,
            String signName) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int)(Math.random()*10);
            builder.append(random);
        }

        String code = builder.toString();

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam",code);
        try {
            CommonResponse response = client.getCommonResponse(request);
            int status = response.getHttpStatus();
            if(status == 200){
                return ResultEntity.successWithData(code);
            }

            return  ResultEntity.failed(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
