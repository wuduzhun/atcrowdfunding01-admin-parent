package com.atguigu.crowd.test;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdTest {

    Logger logger = LoggerFactory.getLogger(CrowdTest.class);

    @Test
    public void testSMS()
    {
        String host = "http://toushitz.market.alicloudapi.com";
        String path = "/ts/notifySms";
        String method = "POST";
        String appcode = "85bbdd0b6fb0427fbe74b70f3a9af892";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {   
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String code = builder.toString();

        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15183853058");
        querys.put("param", code);
        querys.put("sign", "151003");
        querys.put("skin", "84683");
        querys.put("tpl_id", "TP18040317");
        Map<String, String> bodys = new HashMap<String, String>();

        

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            logger.info("statusCode = " + statusCode);

            String reasonPhrase = statusLine.getReasonPhrase();

            logger.info("reasonPhrase = " + reasonPhrase);

            System.out.println(response.toString());
            //获取response的body
            logger.info(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
