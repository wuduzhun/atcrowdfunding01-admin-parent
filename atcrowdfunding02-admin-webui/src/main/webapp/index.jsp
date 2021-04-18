<%--
  Created by IntelliJ IDEA.
  User: OY
  Date: 2020/4/26
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <%--http://localhost:8088/atcrowdfunding02adminwebui_war_exploded/test/ssm.html--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <%--<script src="webjars/jquery/3.2.1/dist/jquery.min.js"></script>--%>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>

    <script>
        $(function () {

            $("#btn4").click(function () {
                //准备要发送的数据
                var student = {
                    "stuId":4,
                    "name":"tom",
                    "age":23,
                    "address":{
                            "private":"广东",
                        "city":"深圳",
                        "street":"宝安区固戍一路"
                    },
                    "subjectList":
                        [{
                            "subjectName":"JavaSE",
                            "subjectScore":80
                        },
                        {
                            "subjectName":"JavaEE",
                            "subjectScore":85
                        },
                        {
                            "subjectName":"SpringBoot",
                            "subjectScore":90
                        }],
                    "map":
                        {
                            "k1":"v1",
                            "k2":"v2"
                        }
                };
            //    将JSON对象转换为JSON字符串
                var requestBody = JSON.stringify(student);

            //    发送Ajax请求
                $.ajax({
                    "url":"send/compose/object.json",
                    "type":"post",
                    "data":requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (reponse) {
                        console.log(reponse);
                    },
                    "error":function (reponse) {
                        console.log(reponse);
                    }
                });
            });

            //准备好要发送服务端的数组
            var array = [5,8,12];
            console.log(array);
            console.log(array.length);
            //将JSON数组转换为JSON字符串
            var requestBody = JSON.stringify(array);
            console.log(requestBody);
            console.log(requestBody.length);
            //"['5','8','12']"
            $("#btn3").click(function () {
                $.ajax({
                    "url": "send/array/three.html",
                    "type": "post",
                    "data": requestBody,//请求体
                    "contentType":"application/json;charset=UTF-8",//设置请求体的内容类型，告诉服务器端本次请求体是JSON数据
                    "dataType": "text",
                    "success": function (response) {
                        alert(response);
                    },
                    "error": function (response) {
                        alert(response);
                    }
                });
            });
            $("#btn1").click(function () {
                $.ajax({
                    "url": "send/array/one.html",
                    "type": "post",
                    "data": {
                        "array": [5, 8, 12]
                    },
                    "dataType": "text",
                    "success": function (response) {
                        alert(response);
                    },
                    "error": function (response) {
                        alert(response);
                    }
                });
            });
            $("#btn2").click(function () {
                $.ajax({
                    "url": "send/array/two.html",
                    "type": "post",
                    "data": {
                        "array[0]": 8,
                        "array[1]": 5,
                        "array[2]": 13
                    },
                    "dataType": "text",
                    "success": function (response) {
                        alert(response);
                    },
                    "error": function (response) {
                        alert(response);
                    }
                });
            });

            $("#btn5").click(function () {
                 layer.msg("Layer的弹框");
            });
        });



    </script>
</head>
<body>
<a href="test/ssm.html">测试SSM整合环境</a>
<br>
<br>
<button id="btn1">Send [5,8,12] One</button>
<br>
<br>
<button id="btn2">Send [5,8,12] Two</button>
<br>
<br>
<button id="btn3">Send [5,8,12] Three</button>
<br>
<br>
<button id="btn4">Send Compose Object</button>
<br>
<br>
<button id="btn5">点我弹框</button>

</body>
</html>
